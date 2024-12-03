package com.gmlab.team_benew.project.projectgetmember

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.projectgetdetail.Profile

class ProjectMemberListAdapter(
    private val members: List<Profile>
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_MEMBER = 1
    private val VIEW_TYPE_NO_MEMBER = 0

    override fun getItemViewType(position: Int): Int {
        return if (members.isEmpty()) VIEW_TYPE_NO_MEMBER else VIEW_TYPE_MEMBER
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_MEMBER) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_team_member_card, parent, false)
            MemberViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_no_team_member_card, parent, false)
            NoMemberViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is MemberViewHolder && members.isNotEmpty()) {
            val member = members[position]
            holder.nameTextView.text = member.member.name
            holder.majorTextView.text = member.member.major
            holder.genderTextView.text = member.member.gender
            holder.experienceTextView.text = if (member.projectExperience) "프로젝트 경험: 유" else "프로젝트 경험: 무"
        }
    }

    override fun getItemCount(): Int {
        return if (members.isEmpty()) 1 else members.size
    }

    class MemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val nameTextView: TextView = itemView.findViewById(R.id.tv_project_member_name_data)
        val majorTextView: TextView = itemView.findViewById(R.id.tv_project_member_major_data)
        val genderTextView: TextView = itemView.findViewById(R.id.tv_project_member_sex_data)
        val experienceTextView: TextView = itemView.findViewById(R.id.tv_project_memeber_profile_experience)
    }

    class NoMemberViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}
