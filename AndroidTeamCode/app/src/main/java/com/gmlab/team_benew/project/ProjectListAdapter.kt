package com.gmlab.team_benew.project

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.PopupWindow
import android.widget.TextView
import androidx.appcompat.widget.PopupMenu
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init
import com.gmlab.team_benew.R
import com.gmlab.team_benew.databinding.ItemProjectListInfoBinding
import com.gmlab.team_benew.databinding.ItemProjectNoListBinding

class ProjectListAdapter(
    private val projectList: List<ProjectResponse>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PROJECT = 0
        private const val VIEW_TYPE_NO_PROJECT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return if (projectList.isEmpty() || position >= projectList.size) VIEW_TYPE_NO_PROJECT else VIEW_TYPE_PROJECT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PROJECT) {
            val binding = ItemProjectListInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            ProjectViewHolder(binding, navController)
        } else {
            val binding = ItemProjectNoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            NoProjectViewHolder(binding, navController)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder is ProjectViewHolder && position < projectList.size) {
            val projectItem = projectList[position]
            holder.bind(projectItem)
        }
    }

    override fun getItemCount(): Int = if (projectList.isEmpty()) 1 else projectList.size + 1

    class ProjectViewHolder(
        private val binding: ItemProjectListInfoBinding,
        private val navController: NavController
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(projectItem: ProjectResponse) {
            binding.tvProjectListProjectTitleInfo.text = projectItem.projectName
            binding.tvProjectListProjectPersonInfo.text = "${projectItem.numberOfMembers}명"
            binding.tvProjectListProjectStartDateInfo.text = projectItem.projectStartDate

            binding.popupProjectListEditOrDisband.setOnClickListener { view ->
                showPopupMenu(view.context, view, projectItem)
        }

            binding.ivGoToMyProjectDeatilInProjectList.setOnClickListener {
                val bundle = Bundle().apply {
                    putInt("projectId", projectItem.projectId.toInt())
                }
                navController.navigate(R.id.action_project_list_to_project_deatil, bundle)
            }
    }
        private fun showPopupMenu(context: Context, anchorView: View, projectItem: ProjectResponse) {
            val inflater = LayoutInflater.from(context)
            val popupView = inflater.inflate(R.layout.popup_menu_layout, null)

            val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true)
            popupWindow.elevation = 10f

            val actionEdit = popupView.findViewById<TextView>(R.id.action_edit)
            val actionDelete = popupView.findViewById<TextView>(R.id.action_delete)

            actionEdit.setOnClickListener {
                popupWindow.dismiss()
                navController.navigate(R.id.action_project_list_to_project_edit_detail, Bundle().apply {
                    putParcelable("projectItem", projectItem)
                })
            }

            actionDelete.setOnClickListener {
                popupWindow.dismiss()
                // Perform delete operation here
            }

            // Show the popup window
            popupWindow.showAsDropDown(anchorView, 0, 0)
        }
    }

    class NoProjectViewHolder(private val binding: ItemProjectNoListBinding, private val navController: NavController) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.ivBtnIfNoProjectMake.setOnClickListener {
                navController.navigate(R.id.action_navigation_project_list_to_projectPostDeatilFragment)
            }
        }
    }
}