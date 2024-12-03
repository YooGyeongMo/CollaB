package com.gmlab.team_benew.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.NavController
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.ProjectResponse

class HomeProjectListAdapter(
    private val projectList: List<ProjectResponse?>,
    private val navController: NavController
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_PROJECT = 1
        private const val VIEW_TYPE_EMPTY = 0
    }

    override fun getItemViewType(position: Int): Int {
        return if (projectList[position] == null) VIEW_TYPE_EMPTY else VIEW_TYPE_PROJECT
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_PROJECT) {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.project_preview_list_item, parent, false)
            ProjectViewHolder(view, navController)
        } else {
            val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.project_preview_no_list_item, parent, false)
            EmptyViewHolder(view, navController)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProjectViewHolder && projectList.isNotEmpty()) {
            holder.bind(projectList[position])
        }
    }

    override fun getItemCount(): Int {
        return if (projectList.isEmpty()) 1 else projectList.size
    }

    class ProjectViewHolder(
        itemView: View,
        private val navController: NavController
    ) : RecyclerView.ViewHolder(itemView) {

        private val projectName: TextView = itemView.findViewById(R.id.tv_project_name_data)
        private val projectTitle: TextView = itemView.findViewById(R.id.tv_project_title_data)
        private val projectKind: TextView = itemView.findViewById(R.id.tv_project_kind_data)
        private val projectLevel: TextView = itemView.findViewById(R.id.tv_project_level_data)
        private val projectStartDate: TextView = itemView.findViewById(R.id.tv_proeject_start_date_data)
        private val goToProjectButton: ImageView = itemView.findViewById(R.id.btn_go_to_this_project)

        fun bind(project: ProjectResponse?) {

            if (project != null) {
                projectName.text = project.projectName
                projectTitle.text = project.projectOneLineIntroduction
                projectKind.text = "캡스톤 / 앱"
                projectStartDate.text = project.projectStartDate

                val projectStage = "초기 단계"
                projectLevel.text = projectStage

                goToProjectButton.setOnClickListener {
                    val bundle = Bundle().apply {
                        putInt("projectId", project.projectId.toInt())
                    }
                    navController.navigate(R.id.navigation_project_deatil, bundle)
                }
            }
        }
    }

    class EmptyViewHolder(
        itemView: View,
        private val navController: NavController
    ) : RecyclerView.ViewHolder(itemView) {

        private val goToMakeProjectButton: ImageView =
            itemView.findViewById(R.id.btn_go_to_make_project)

        init {
            goToMakeProjectButton.setOnClickListener {
                navController.navigate(R.id.action_home_to_navigation_project_post_deatil)
            }
        }
    }

}
