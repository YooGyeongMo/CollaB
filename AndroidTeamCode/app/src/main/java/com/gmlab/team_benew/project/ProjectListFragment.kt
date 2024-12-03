package com.gmlab.team_benew.project

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.databinding.FragmentIntroMyprojectBinding

class ProjectListFragment : Fragment(), ProjectListView {

    private var _binding: FragmentIntroMyprojectBinding? = null
    private val binding get() = _binding!!
    private lateinit var projectListAdapter: ProjectListAdapter
    private val projectListViewModel: ProjectListViewModel by viewModels()
    private lateinit var projectListService: ProjectListService
    private lateinit var recyclerView: RecyclerView
    private lateinit var noProjectTextView: TextView
    private lateinit var noProjectTextView2: TextView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentIntroMyprojectBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 프로젝트리스트 서비스 초기화
        projectListService = ProjectListService(requireContext())
        projectListService.setProjectListView(this)

        // 어뎁터랑 리사이클러뷰 초기화
        projectListAdapter = ProjectListAdapter(emptyList(), findNavController())
        binding.myRecyclerViewProjectList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = projectListAdapter
        }

        // Observe 뷰모델 데이터
        projectListViewModel.projects.observe(viewLifecycleOwner, Observer { projects ->
            projectListAdapter = ProjectListAdapter(projects,  findNavController())
            binding.myRecyclerViewProjectList.adapter = projectListAdapter

            if(projects.isNullOrEmpty()){
                binding.myRecyclerViewProjectList.visibility = View.GONE
                binding.tvWelcomeNoMyProjectIntro.visibility = View.VISIBLE
                binding.tvWelcomeNoMyProjectIntro2.visibility = View.VISIBLE
            } else {
                binding.myRecyclerViewProjectList.visibility = View.VISIBLE
                binding.tvWelcomeNoMyProjectIntro.visibility = View.GONE
                binding.tvWelcomeNoMyProjectIntro2.visibility = View.GONE
            }
        })

        projectListViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            binding.myProjectLoadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        // Load project data
        projectListViewModel.loadProjects(projectListService)

        // 결과 수신
        parentFragmentManager.setFragmentResultListener("projectPostResult", this) { key, bundle ->
            val isSuccess = bundle.getBoolean("isSuccess")
            if (isSuccess) {
                showAlert("프로젝트 생성 성공", "프로젝트가 성공적으로 생성되었습니다.")
            } else {
                showAlert("프로젝트 생성 실패", "프로젝트 생성에 실패했습니다.")
            }
        }
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProjectListSuccess(projects: List<ProjectResponse>) {
        projectListViewModel.setProjects(projects)
        Log.d("ProjectListFragment", "프로젝트 전체 리스트 불러오는데 성공! 200")
    }

    override fun onProjectListEmpty() {
        projectListViewModel.setProjects(emptyList())
        Log.e("ProjectListFragment", "프로젝트 전체 리스트가 비어있습니다.")
    }

    override fun onProjectListFailure() {
        Log.e("ProjectListFragment", "프로젝트 전체 리스트 불러오는데 실패 401에러")
    }

    override fun onProjectListForbidden() {
        Log.e("ProjectListFragment", "접근 금지 403에러")
    }

    override fun onProjectListNotFound() {
        Log.e("ProjectListFragment", "프로젝트 전체 리스트를 찾을수 없음 404에러")
    }
}