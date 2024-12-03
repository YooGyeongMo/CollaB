package com.gmlab.team_benew.matching.projectmatchingfind

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.airbnb.lottie.LottieAnimationView
import com.airbnb.lottie.LottieDrawable
import com.gmlab.team_benew.R
import com.gmlab.team_benew.matching.SharedViewModel

class ProjectFindingAllFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var viewModel: ProjectFindingAllViewModel
    private lateinit var adapter: ProjectFindingAllAdapter
    private lateinit var loadingAnimation: LottieAnimationView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_find_project_of_all_project, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        loadingAnimation = view.findViewById(R.id.finding_all_project_loading_indicator)

        val recyclerView = view?.findViewById<RecyclerView>(R.id.my_recycler_view_all_finding_project_list)
        recyclerView?.layoutManager = LinearLayoutManager(context)
        adapter = ProjectFindingAllAdapter()
        recyclerView?.adapter = adapter
        // 팩토리를 사용하여 ViewModel 초기화
        val factory = ProjectFindingAllViewModelFactory(sharedViewModel)
        viewModel = ViewModelProvider(this, factory).get(ProjectFindingAllViewModel::class.java)

        viewModel.projects.observe(viewLifecycleOwner, Observer { projects ->

        })

        viewModel.errorMessage.observe(viewLifecycleOwner, Observer { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        })
        val token = getTokenFromSharedPreferences()
        token?.let {
            viewModel.fetchProjects(it)
        }


        // 처음부터 애니메이션 재생
        loadingAnimation.visibility = View.VISIBLE
        loadingAnimation.repeatCount = LottieDrawable.INFINITE
        loadingAnimation.playAnimation()

        // 4초 후 로딩 애니메이션을 중지하고 데이터 바인딩
        Handler(Looper.getMainLooper()).postDelayed({
            // 로딩 상태 확인 후 UI 업데이트
            if (viewModel.isLoading.value == false) {
                adapter.submitList(viewModel.projects.value) // 데이터 바인딩
            }
            loadingAnimation.visibility = View.GONE
            loadingAnimation.pauseAnimation()
        }, 4500) // 4초 지연



    }



    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getString("userToken", null)
    }
}
