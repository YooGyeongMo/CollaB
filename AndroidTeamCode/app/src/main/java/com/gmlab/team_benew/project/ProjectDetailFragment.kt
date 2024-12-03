package com.gmlab.team_benew.project


import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.projectfinishpatch.ProjectDeatilFinishPatchView
import com.gmlab.team_benew.project.projectfinishpatch.ProjectDetailFinishPatchResponse
import com.gmlab.team_benew.project.projectfinishpatch.ProjectDetailFinishPatchService
import com.gmlab.team_benew.project.projectgetdetail.GetProjectDeatilResponse
import com.gmlab.team_benew.project.projectgetdetail.ProjectDetailService
import com.gmlab.team_benew.project.projectgetdetail.ProjectDetailView
import com.gmlab.team_benew.project.projectgetdetail.ProjectDetailViewModel
import com.gmlab.team_benew.project.projectgetmember.ProjectGetMemberViewModel
import com.gmlab.team_benew.project.projectgetmember.ProjectMemberListAdapter
import com.gmlab.team_benew.project.projectstartpatch.ProjectDetailStartPatchResponse
import com.gmlab.team_benew.project.projectstartpatch.ProjectDetailStartPatchService
import com.gmlab.team_benew.project.projectstartpatch.ProjectDetailStartPatchView
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class ProjectDetailFragment : Fragment(), ProjectDetailView, ProjectDetailStartPatchView,
    ProjectDeatilFinishPatchView {

    private lateinit var projectDetailService: ProjectDetailService
    private lateinit var projectStartPatchService: ProjectDetailStartPatchService
    private lateinit var projectFinishPatchService: ProjectDetailFinishPatchService
    private val projectDetailViewModel: ProjectDetailViewModel by viewModels()
    private val cal = Calendar.getInstance()


    private lateinit var projectNameTextView: TextView
    private lateinit var projectStartDateTextView: TextView
    private lateinit var projectEndDateTextView: TextView
    private lateinit var projectOneLineIntroTextView: TextView
    private lateinit var projectIntroTextView: TextView
    private lateinit var projectProgressBar: ProgressBar
    private lateinit var loadingIndicator: ProgressBar
    private lateinit var projectDetailContent: View
    private lateinit var memberViewPager: ViewPager2
    private lateinit var projectmemberCount: TextView
    private lateinit var dotsIndicator: DotsIndicator
    private lateinit var projectStartBtn: Button
    private lateinit var projectFinishBtn: Button

    private lateinit var selectedDate: String


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_my_project_detail, container, false)

        projectDetailContent = view.findViewById(R.id.project_detail_content)
        projectNameTextView = view.findViewById(R.id.tv_project_title_name_of_detail)
        projectStartDateTextView = view.findViewById(R.id.tv_project_detail_start_date_data)
        projectEndDateTextView = view.findViewById(R.id.tv_project_detail_end_date_data)
        projectOneLineIntroTextView =
            view.findViewById(R.id.tv_project_detail_summary_introduction_info_data)
        projectIntroTextView =
            view.findViewById(R.id.tv_project_detail_explain_introduction_info_data)
        projectProgressBar = view.findViewById(R.id.pb_project_detail_state_data)
        loadingIndicator = view.findViewById(R.id.project_detail_loading_indicator)
        memberViewPager = view.findViewById(R.id.viewPager_team_members)
        projectmemberCount = view.findViewById(R.id.tv_project_team_memeber_count_data)
        dotsIndicator = view.findViewById(R.id.did_project_member)
        projectStartBtn = view.findViewById(R.id.btn_project_start)
        projectFinishBtn = view.findViewById(R.id.btn_project_finish)

        projectDetailService = ProjectDetailService(requireContext())
        projectStartPatchService = ProjectDetailStartPatchService(requireContext())
        projectFinishPatchService = ProjectDetailFinishPatchService(requireContext())
        projectDetailService.setProjectDetailView(this)

        projectDetailViewModel.projectDetail.observe(viewLifecycleOwner, Observer { projectDetail ->
            updateUI(projectDetail)
        })

        projectDetailViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            if (isLoading) {
                loadingIndicator.visibility = View.VISIBLE
                projectDetailContent.visibility = View.GONE
            } else {
                loadingIndicator.visibility = View.GONE
                projectDetailContent.visibility = View.VISIBLE
            }
        })


        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //번들에서 projectId가져옴
        val projectId = arguments?.getInt("projectId")

        //project를 UI에 표시하거나 로그에 출력
        if (projectId != null) {
            // 로그에 출력
            projectDetailViewModel.setLoading(true)
            projectDetailService.getProjectDetail(projectId)
            Log.d("ProjectDetailFragment", "프로젝트 id: $projectId")
        } else {
            Log.e("ProjectDetailFragment", "No projectId")
        }
        projectStartBtn.setOnClickListener {
            DatePickerDialog(
                requireContext(),
                { _, y, m, d ->
                    cal.set(Calendar.YEAR, y)
                    cal.set(Calendar.MONTH, m)
                    cal.set(Calendar.DAY_OF_MONTH, d)
                    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                    selectedDate = dateFormat.format(cal.time)
                    projectEndDateTextView.text = selectedDate
                    Log.d("ProjectDetailFragment", "선택된 날짜: $selectedDate")

                    // PATCH 요청 보내기
                    if (projectId != null) {
                        projectStartPatchService.patchProjectStart(
                            projectId,
                            selectedDate,
                            this@ProjectDetailFragment
                        )
                    }
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
        projectFinishBtn.setOnClickListener {
            if (projectId != null) {
                projectFinishPatchService.projectFinishPatch(projectId, this@ProjectDetailFragment)
            }
        }
    }

    override fun onProjectDetailSuccess(projectDetail: GetProjectDeatilResponse) {
        projectDetailViewModel.setLoading(false)
        projectDetailViewModel.setProjectDetail(projectDetail)
        Log.d("ProjectDetailFragment", "프로젝트 상세 정보를 불러오는데 성공!")

        memberViewPager.adapter = ProjectMemberListAdapter(projectDetail.profiles)
        dotsIndicator.setViewPager2(memberViewPager)
    }

    override fun onProjectDetailFailure(statusCode: Int) {
        projectDetailViewModel.setLoading(false)
        Log.d("ProjectDetailFragment", "프로젝트 상세 정보를 불러오는데 성공!")
    }

    private fun updateUI(projectDetail: GetProjectDeatilResponse) {
        projectNameTextView.text = "${projectDetail.projectName} 프로젝트"

        if (projectDetail.projectStartDate.isNullOrEmpty() &&
            projectDetail.projectDeadlineDate.isNullOrEmpty()
        ) {
            projectStartDateTextView.text = "프로젝트 마감일을 정해주세요"
            projectEndDateTextView.text = "프로젝트 마감일을 정해주세요"
            projectStartBtn.visibility = View.VISIBLE
            projectFinishBtn.visibility = View.GONE  // 프로젝트 종료 버튼 숨김
        } else {

            projectStartBtn.visibility = View.GONE
            projectStartDateTextView.text = projectDetail.projectStartDate
            projectEndDateTextView.text = projectDetail.projectDeadlineDate

            if (projectDetail.projectStarted) {
                projectFinishBtn.visibility = View.VISIBLE// 프로젝트 종료버튼보임
            } else {
                projectFinishBtn.visibility = View.GONE
            }
        }
        projectOneLineIntroTextView.text = projectDetail.projectOneLineIntroduction
        projectIntroTextView.text = projectDetail.projectIntroduction
        projectmemberCount.text = "${projectDetail.numberOfMembers} 명"

    }

    override fun onSuccessProjectStartPatch(response: ProjectDetailStartPatchResponse) {
        // 성공적으로 PATCH 요청이 완료되었을 때
        showAlert("프로젝트 시작", "프로젝트가 성공적으로 시작되었습니다.")
    }

    override fun onFailureProjectStartPatch(message: String) {
        // PATCH 요청이 실패했을 때
        showAlert("프로젝트 시작 성공!", "프로젝트가 성공적으로 시작되었습니다.!")
    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext())
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("확인") { _, _ ->
                // 데이터를 새로 불러와서 업데이트
                val projectId = arguments?.getInt("projectId")
                if (projectId != null) {
                    projectDetailViewModel.setLoading(true)
                    projectDetailService.getProjectDetail(projectId)
                }
            }
            .show()
    }

    override fun onSuccessProjectFinishPatch(response: ProjectDetailFinishPatchResponse) {
        val projectName = projectDetailViewModel.projectDetail.value?.projectName
        showAlert("프로젝트 종료 성공", "${projectName}프로젝트가 성공적으로 종료되었습니다.")
    }

    override fun onFailureProjectFinishPatch(message: String) {
        val projectName = projectDetailViewModel.projectDetail.value?.projectName
        showAlert("프로젝트 종료 실패", "${projectName}프로젝트가 종료 되지 않았습니다.")
    }
}