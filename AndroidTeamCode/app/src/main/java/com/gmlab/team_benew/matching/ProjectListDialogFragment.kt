package com.gmlab.team_benew.matching

import android.app.AlertDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.ProjectResponse

class ProjectListDialogFragment : DialogFragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels()
    private var selectedProject: ProjectResponse? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.project_list_modal_of_matching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val projects = sharedViewModel.projects.value ?: emptyList()
        val spinner: Spinner = view.findViewById(R.id.sp_project_list_modal)

        val adapter = ModalProjectAdapter(requireContext(), projects)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long)
            {
                selectedProject = adapter.getItem(position)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Do nothing
            }
        }

        view.findViewById<Button>(R.id.btn_enter_modal_project).setOnClickListener {

            if (selectedProject == null) {
                showAlertDialog() // 경고 다이얼로그 표시
            } else {
                sharedViewModel.setSelectedProject(selectedProject!!)
                dismiss()
                findNavController().navigate(R.id.action_intro_matching_to_navigation_matching)
            }
        }


        view.findViewById<Button>(R.id.btn_cancel_modal_project).setOnClickListener {
            dismiss()
        }
    }

    private fun showAlertDialog() {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("프로젝트 선택")
            setMessage("프로젝트를 선택해주세요.")
            setPositiveButton("확인") { dialog, which -> dialog.dismiss() }
            show()
        }
    }
}