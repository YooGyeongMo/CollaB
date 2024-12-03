package com.gmlab.team_benew.matching

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.gmlab.team_benew.R
import com.gmlab.team_benew.project.ProjectListService
import com.gmlab.team_benew.project.ProjectListView
import com.gmlab.team_benew.project.ProjectResponse

class MatchingIntroFragment:Fragment(), ProjectListView {

    private val handler = Handler()
    private val sharedViewModel: SharedViewModel by activityViewModels()
    private lateinit var imageView: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_intro_matching, container, false)
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val buttonTeamSearchingIntro = view.findViewById<Button>(R.id.btn_team_searching_intro)
        val buttonNavMatchingDetail = view.findViewById<Button>(R.id.btn_coworker_searching_intro)
        imageView = view.findViewById<ImageView>(R.id.iv_matching_intro_gif)
        val progressBar = view.findViewById<ProgressBar>(R.id.intro_matching_indicator)
        val buttonCreateProject = view.findViewById<Button>(R.id.btn_if_no_project_show_this_button)
        // imageView 초기화


        // 모든 버튼에 같은 클릭 리스너 설정
        buttonNavMatchingDetail.setOnClickListener { showProjectListModal() }
        buttonCreateProject.setOnClickListener { onCreateProjectClicked() }
        buttonTeamSearchingIntro.setOnClickListener{onProjectAllFindingClicked()}


        loadGifWithGlide(imageView, progressBar)

        val projectListService = ProjectListService(requireContext())
        projectListService.setProjectListView(this)
        projectListService.getProjects()
    }

    private fun onProjectAllFindingClicked() {
           findNavController().navigate(R.id.action_matching_intro_to_project_all_finding) //모든프로젝트 찾기로 이동
    }

    private fun onCreateProjectClicked() {
    }

    private fun loadGifWithGlide(imageView: ImageView, progressBar: ProgressBar) {
        Glide.with(this)
            .asGif()
            .load("file:///android_asset/matching_intro_new_2.gif")
            .diskCacheStrategy(DiskCacheStrategy.ALL) // 디스크 캐시를 최적화
            .into(object : CustomTarget<GifDrawable>() {
                override fun onResourceReady(resource: GifDrawable, transition: Transition<in GifDrawable>?) {
                    imageView.setImageDrawable(resource)
                    resource.start()
                    progressBar.visibility = View.GONE // GIF 로드가 완료되면 프로그레스바 숨기기

                    // 6초 후에 GIF를 멈춤
                    handler.postDelayed({
                        resource.stop()
                    }, 6000)
                }

                override fun onLoadCleared(placeholder: Drawable?) {
                    // 필요 시 처리
                }
            })
    }
    override fun onProjectListSuccess(projects: List<ProjectResponse>) {
        sharedViewModel.setProjects(projects)
        view?.findViewById<Button>(R.id.btn_coworker_searching_intro)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.btn_if_no_project_show_this_button)?.visibility = View.GONE
        Log.d("FragmentMatchingIntro","프로젝트 리스트 불러오기 200 ! 성공")
    }

    override fun onProjectListEmpty() {
        view?.findViewById<Button>(R.id.btn_if_no_project_show_this_button)?.visibility = View.VISIBLE
        view?.findViewById<Button>(R.id.btn_coworker_searching_intro)?.visibility = View.GONE
        view?.findViewById<Button>(R.id.btn_team_searching_intro)?.visibility = View.GONE
        Log.e("FragmentMatchingIntro","프로젝트 리스트 불러오기 Null")
    }

    override fun onProjectListFailure() {
        Log.e("HomeFragment", "프로젝트 리스트 불러오는데 실패 401에러")
    }

    override fun onProjectListForbidden() {
        Log.e("HomeFragment", "접근 금지 403에러")
    }

    override fun onProjectListNotFound() {
        Log.e("HomeFragment", "프로젝트 리스트를 찾을 수 없음 404에러")
    }

    private fun showProjectListModal() {
        val dialog = ProjectListDialogFragment()
        dialog.show(childFragmentManager, "ProjectListDialogFragment")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        imageView.setImageDrawable(null) // 이미지뷰의 이미지 리소스 해제
        handler.removeCallbacksAndMessages(null) // 핸들러의 콜백 및 메시지 제거
    }
}