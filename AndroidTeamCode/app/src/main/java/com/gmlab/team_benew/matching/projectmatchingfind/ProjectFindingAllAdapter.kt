package com.gmlab.team_benew.matching.projectmatchingfind

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import org.w3c.dom.Text

class ProjectFindingAllAdapter :
    ListAdapter<ProjectFindingAllResponse, ProjectFindingAllAdapter.ProjectViewHolder>(ProjectDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProjectViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_finding_project_of_all_project, parent, false)
        return ProjectViewHolder(view)
    }

    override fun onBindViewHolder(holder: ProjectViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ProjectViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val projectName: TextView = itemView.findViewById(R.id.tv_finding_project_title_data)
        private val numberOfMembers: TextView = itemView.findViewById(R.id.tv_finding_project_person_data)
        private val startDate: TextView = itemView.findViewById(R.id.tv_finding_project_start_date_data)
        private val requestToJoin: TextView = itemView.findViewById(R.id.tv_suggest_to_project_member)

        fun bind(project: ProjectFindingAllResponse) {
            projectName.text = project.projectName
            numberOfMembers.text = "${project.numberOfMembers} 명"
            startDate.text = project.startDate

            requestToJoin.setOnClickListener {
                showAlertDialog(itemView.context, project.projectName)
            }
        }
        private fun showAlertDialog(context: Context, projectName: String) {
            val builder = AlertDialog.Builder(context)
            builder.setTitle("참여 요청")
            builder.setMessage("프로젝트 팀장에게 요청을 보냈습니다.")
            builder.setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
            }
            builder.show()
        }

    }
}

class ProjectDiffCallback : DiffUtil.ItemCallback<ProjectFindingAllResponse>() {
    override fun areItemsTheSame(oldItem: ProjectFindingAllResponse, newItem: ProjectFindingAllResponse): Boolean {
        return oldItem.projectId == newItem.projectId
    }

    override fun areContentsTheSame(oldItem: ProjectFindingAllResponse, newItem: ProjectFindingAllResponse): Boolean {
        return oldItem == newItem
    }
}
