package com.gmlab.team_benew.project.projectfinishpatch

import android.content.Context
import com.gmlab.team_benew.auth.getRetrofit
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProjectDetailFinishPatchService(private val context: Context) {
    private val retrofit = getRetrofit()

    private val api = retrofit.create(ProjectDetailFinishPatchRetrofitInterface::class.java)

    fun projectFinishPatch(projectId:Int,view: ProjectDeatilFinishPatchView){
        val token = getTokenFromSharedPreferences()
        if (token != null) {
            val bearerToken = "Bearer $token"
            val call = api.patchProjectFinish(bearerToken, projectId)

            call.enqueue(object: Callback<ProjectDetailFinishPatchResponse> {
                override fun onResponse(
                    call: Call<ProjectDetailFinishPatchResponse>,
                    response: Response<ProjectDetailFinishPatchResponse>
                ){
                    if(response.isSuccessful) {
                        view.onSuccessProjectFinishPatch(response.body()!!)
                    } else{
                        view.onFailureProjectFinishPatch("Error: ${response.code()}")
                    }
                }

                override fun onFailure(call: Call<ProjectDetailFinishPatchResponse>, t: Throwable) {
                    view.onFailureProjectFinishPatch(t.message ?: "Unknown error")
                }
            })

        }
        else {
            view.onFailureProjectFinishPatch("Token UnFound")
        }

    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

}