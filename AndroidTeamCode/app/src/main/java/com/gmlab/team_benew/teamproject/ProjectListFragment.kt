package com.gmlab.team_benew.teamproject

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.gmlab.team_benew.R
import com.gmlab.team_benew.Todolist.ProjectData
import com.gmlab.team_benew.Todolist.TodoListCardAdapter
import com.gmlab.team_benew.databinding.FragmentMyprojectListBinding

class ProjectListFragment:Fragment() {
    val binding by lazy{ FragmentMyprojectListBinding.inflate(layoutInflater)}



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?): View? {


        val mymodellist=getMymodel()
        val adapter1= TodoListCardAdapter()
        adapter1.modelList=mymodellist
        binding.myRecyclerView.adapter=adapter1
        binding.myRecyclerView.layoutManager= LinearLayoutManager(context)

        return binding.root
        //return inflater.inflate(R.layout.fragment_chatlist,container,false)

    }
    fun getMymodel():MutableList<ProjectData>{
        var mymodellist= mutableListOf<ProjectData>()
        for (i in 1..10) {
            val todoName= "myproject_$i"
            val mymodeldata=ProjectData(todoName)
            mymodellist.add(mymodeldata)
        }
        return mymodellist
    }

}