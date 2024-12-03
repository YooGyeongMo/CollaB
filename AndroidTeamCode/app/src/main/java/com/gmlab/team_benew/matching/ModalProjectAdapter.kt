package com.gmlab.team_benew.matching

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.ProjectResponse

class ModalProjectAdapter(
    context: Context,
    private  val projects: List<ProjectResponse>
) : ArrayAdapter<ProjectResponse>(context, 0, projects) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_project_list_modal, parent, false)
        val project = getItem(position)

        val projectNameTextView: TextView = view.findViewById(R.id.tv_project_name_modal_data)
        projectNameTextView.text = project?.projectName?: "프로젝트 선택"

        return view
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        return getView(position, convertView, parent)
    }

    override fun getItem(position: Int): ProjectResponse? {
        return if (position == 0) null else projects[position - 1]
    }

    override fun getCount(): Int {
        return projects.size + 1
    }
}