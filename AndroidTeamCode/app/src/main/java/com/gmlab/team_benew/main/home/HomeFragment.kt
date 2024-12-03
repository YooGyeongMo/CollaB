package com.gmlab.team_benew.main.home

import android.app.AlertDialog
import android.app.ProgressDialog.show
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Base64
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.gmlab.team_benew.R
import com.gmlab.team_benew.auth.getRetrofit
import com.gmlab.team_benew.main.MainAuthService
import com.gmlab.team_benew.main.MainView
import com.gmlab.team_benew.main.UserNameCallback
import com.gmlab.team_benew.main.home.firstSetting.FirstSettingActivity
import com.gmlab.team_benew.main.home.firstSetting.getNicknameData
import com.gmlab.team_benew.main.home.firstSetting.getNicknameRequest
import com.gmlab.team_benew.profile.getProfileDetailData
import com.gmlab.team_benew.project.ProjectListService
import com.gmlab.team_benew.project.ProjectListView
import com.gmlab.team_benew.project.ProjectResponse
import com.gmlab.team_benew.test.CodingTestActivity
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator
import okio.ByteString.Companion.decodeBase64
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

class HomeFragment : Fragment(), MainView, UserNameCallback, HomeView, ProjectListView {

    private lateinit var viewPager: ViewPager2
    private lateinit var viewPager2: ViewPager2
    private lateinit var textIndicator: TextView
    private lateinit var textIndicatorData: TextView
    private lateinit var homeService: HomeService
    private lateinit var profileImageView: ImageView
    private lateinit var peerImageView: ImageView
    private lateinit var loadingIndicator: ProgressBar
    private var viewPager2Indicator: DotsIndicator? = null
    private lateinit var mainProjectNameTextView: TextView
    private lateinit var mainProjectDdayTextView: TextView
    private lateinit var mainProjectProgressBar: ProgressBar
    private lateinit var mainProjectLoadingIndicator: ProgressBar
    private lateinit var noMainProjectData: TextView

    private val homeViewModel: HomeViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onResume() {
        super.onResume()
        getUserInfo() // 사용자 정보를 새로고침하는 메서드 호출
        getUserProfileInfo()
        loadProjects()
        // 데이터 로드 호출
        getMainProjectData()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewPager = view.findViewById(R.id.vp_home_banner)
        textIndicator = view.findViewById(R.id.tv_home_banner_text_indicator)
        textIndicatorData = view.findViewById(R.id.tv_home_banner_text_indicator_data)
        profileImageView = view.findViewById(R.id.iv_profile_info_pic)
        peerImageView = view.findViewById(R.id.home_profile_preview_peer_review_weather)
        loadingIndicator = view.findViewById(R.id.loading_indicator)
        viewPager2 = view.findViewById(R.id.vp_home_project_list)
        viewPager2Indicator = view.findViewById(R.id.did_project_list)
        mainProjectNameTextView = view.findViewById(R.id.tv_home_my_main_project_name_info)
        mainProjectDdayTextView = view.findViewById(R.id.tv_home_my_main_project_d_day_data)
        mainProjectProgressBar = view.findViewById(R.id.pb_home_main_project_percent_state)
        mainProjectLoadingIndicator = view.findViewById(R.id.main_project_loading_indicator)
        noMainProjectData = view.findViewById(R.id.no_main_project)

        // 버튼 클릭 리스너 설정
        val buttonSkillTest = view.findViewById<Button>(R.id.btn_skill_test)
        buttonSkillTest.setOnClickListener { onButtonClicked(it) }

        //김대환 : db에 닉네임이 없으면 초기 프로필 작성 액티비티를 띄운다
        //초기 사진 닉네임 세팅 없이 테스트 하고 싶으면 주석처리 할 것
        checkNicknameIsEmpty()

        // ViewModel의 프로필 데이터를 관찰합니다.
        homeViewModel.profileData.observe(viewLifecycleOwner, Observer { profileData ->
            profileData?.let { updateProfileUI(it) }
        })

        // ViewModel의 프로젝트 데이터를 관찰합니다.
        homeViewModel.projectList.observe(viewLifecycleOwner, Observer { projects ->
            updateProjectList(projects)
        })

        // ViewModel의 로딩 상태를 관찰합니다.
        homeViewModel.isLoading.observe(viewLifecycleOwner, Observer { isLoading ->
            loadingIndicator.visibility = if (isLoading) View.VISIBLE else View.GONE
        })

        homeViewModel.mainProjectData.observe(viewLifecycleOwner, Observer { projectData ->
            projectData?.let { updateMainProjectUI(it) }
        })


        // 여기서 사용자 정보를 가져옴
        getUserInfo()
        //여기서 사용자 프로젝트 프로필 미리보기 데이터 가져오기
        getUserProfileInfo()
        // 프로젝트 목록을 로드합니다.
        loadProjects()
        // 데이터 로드 호출
        getMainProjectData()


        // 이미지 리스트
        val imageList = listOf(
            R.drawable.home_banner_1,
            R.drawable.home_banner_2,
            R.drawable.home_banner_3,
            R.drawable.home_banner_4
        )

        // 어댑터 설정
        val adapter = HomeBannerImageAdapter(imageList)
        viewPager.adapter = adapter
        // 초기 페이지 설정
        viewPager.setCurrentItem(0, false)

        // 텍스트 인디케이터 초기화
        updateTextIndicator(0, imageList.size)
        // 페이지 변경 리스너 설정
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                updateTextIndicator(position, imageList.size)
            }
        })

        // PageTransformer 설정
        val pageWidth = resources.getDimension(R.dimen.viewpager_item_width)
        val pageMargin = resources.getDimension(R.dimen.viewpager_item_margin)
        val screenWidth = resources.displayMetrics.widthPixels
        val offsetPx = screenWidth - pageWidth - pageMargin

        viewPager.offscreenPageLimit = 3

        viewPager.setPageTransformer { page, position ->
            page.translationX = position * -offsetPx
        }


        val buttonNavProfile = view.findViewById<CardView>(R.id.cv_user_info_card)
//        val buttonNavProject = view.findViewById<CardView>(R.id.cv_project_info_card)
//        val buttonNavMyteamlist = view.findViewById<CardView>(R.id.cv_my_team_list)
//        val buttonNavTestIntro = view.findViewById<Button>(R.id.btn_do_test)

        // 모든 버튼에 같은 클릭 리스너 설정
        buttonNavProfile.setOnClickListener { onCardClicked(it) }
//        buttonNavProject.setOnClickListener { onCardClicked(it) }
//        buttonNavMyteamlist.setOnClickListener { onCardClicked(it) }
//        buttonNavTestIntro.setOnClickListener{ onCardClicked(it) }

        // 결과 수신
        parentFragmentManager.setFragmentResultListener("projectPostResult", this) { key, bundle ->
            val isSuccess = bundle.getBoolean("isSuccess")
            if (isSuccess) {
                showAlert("프로젝트 생성 성공", "프로젝트가 성공적으로 생성되었습니다.")
            } else {
                showAlert("프로젝트 생성 실패", "프로젝트 생성에 실패했습니다.")
            }
        }


    }

    private fun showAlert(title: String, message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(message)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            create()
            show()
        }
    }

    private fun loadProjects() {
        val projectListService = ProjectListService(requireContext())
        projectListService.setProjectListView(this) // HomeFragment를 ProjectListView로 설정
        projectListService.getProjects() // 프로젝트 목록 가져오기
    }

    private fun updateProjectList(projects: List<ProjectResponse?>) {
        val adapter: HomeProjectListAdapter

        if (projects.isNotEmpty()) {
            adapter = HomeProjectListAdapter(projects, findNavController())
            viewPager2.adapter = adapter // 어댑터 설정을 먼저 합니다.
            viewPager2Indicator?.setViewPager2(viewPager2)
            viewPager2Indicator?.visibility = View.VISIBLE
        } else {
            // 프로젝트 리스트가 비어 있을 때 빈 아이템 추가
            val emptyProjectList = listOf<ProjectResponse?>(null)
            adapter = HomeProjectListAdapter(emptyProjectList, findNavController())
            viewPager2.adapter = adapter // 어댑터 설정을 먼저 합니다.
            viewPager2Indicator?.visibility = View.GONE
        }


        // 초기 페이지 설정
        viewPager2.setCurrentItem(0, false)

        // 페이지 변경 리스너 설정
        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })
    }


    private fun updateTextIndicator(position: Int, total: Int) {
        val currentPosition = (position % total) + 1
        textIndicatorData.text = currentPosition.toString()
        textIndicator.text = "/$total"

        if (currentPosition == 4) {
            textIndicatorData.setTextColor(resources.getColor(R.color.black, null))
            textIndicator.setTextColor(resources.getColor(R.color.black, null))
        } else {
            textIndicatorData.setTextColor(resources.getColor(R.color.white, null))
            textIndicator.setTextColor(resources.getColor(R.color.white, null))
        }
    }

    private fun updateMainProjectUI(projectData: getMainProjectData) {
        if (
            projectData.projectRateOfProgress >= 0
        ) {
            noMainProjectData.visibility = View.GONE
            mainProjectProgressBar.visibility = View.VISIBLE
            mainProjectDdayTextView.visibility = View.VISIBLE
            mainProjectNameTextView.visibility = View.VISIBLE

            mainProjectNameTextView.text = projectData.projectName

            // double 값을 올림하여 int로 변환
            val progressRate = Math.ceil(projectData.projectRateOfProgress).toInt()
            mainProjectDdayTextView.text = "$progressRate%"
            mainProjectProgressBar.progress = progressRate

        } else if (projectData.projectRateOfProgress < 0) {
            noMainProjectData.visibility = View.VISIBLE
            mainProjectProgressBar.visibility = View.GONE
            mainProjectDdayTextView.visibility = View.GONE
            mainProjectNameTextView.visibility = View.GONE
        } else {
            noMainProjectData.visibility = View.VISIBLE
            mainProjectProgressBar.visibility = View.GONE
            mainProjectDdayTextView.visibility = View.GONE
            mainProjectNameTextView.visibility = View.GONE
        }
    }

    private fun getMainProjectData() {
        mainProjectLoadingIndicator.visibility = View.VISIBLE // 로딩 인디케이터 표시
        val homeService = HomeService(requireContext())
        homeService.setHomeView(this)
        homeService.getMainProjectData()
    }

    override fun onMainProjectGetSuccess(projectData: getMainProjectData) {
        mainProjectLoadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
        Log.d("HomeFragment", "메인 프로젝트 데이터 성공: $projectData")
        homeViewModel.setMainProjectData(projectData)
    }

    override fun onMainProjectGetFailure(statusCode: Int) {
        mainProjectLoadingIndicator.visibility = View.GONE // 로딩 인디케이터 숨기기
        Log.e("HomeFragment", "메인 프로젝트 데이터 실패:  $statusCode")
    }

    private fun onCardClicked(view: View) {
        when (view.id) {
            R.id.cv_user_info_card -> findNavController().navigate(R.id.action_home_to_profileDetail) // 프로필 디테일로 이동
//            R.id.cv_project_info_card -> findNavController().navigate(R.id.action_home_to_projectList) // 프로젝트 리스트로
//            R.id.cv_my_team_list -> findNavController().navigate(R.id.action_home_to_teamList) // 팀 리스트로
//            R.id.btn_do_test -> findNavController().navigate(R.id.action_home_to_intro_testing) // testing 화면으로
        }
    }

    private fun onButtonClicked(view: View) {
        when (view.id) {
            R.id.btn_skill_test -> {
                val intent = Intent(requireContext(), CodingTestActivity::class.java)
                startActivity(intent)
            }
        }
    }

    private fun getUserProfileInfo() {
        homeViewModel.setLoading(true) // 로딩 시작
        homeService = HomeService(requireContext())
        homeService.setHomeView(this)
        homeService.getUserProfilePreviewData()
    }

    private fun updateProfileUI(profileData: getProfilePreiviewData) {

        val photoUrl = profileData.photo

        if (photoUrl.isNullOrEmpty()) {
            // photo가 null이거나 빈 문자열인 경우
            Glide.with(this)
                .load(R.drawable.male_avatar)
                .apply(RequestOptions.circleCropTransform())
                .into(profileImageView)
        } else {
            // photo가 Base64 문자열인 경우
            val bitmap = decodeBase64(photoUrl)
            bitmap?.let {
                Glide.with(this)
                    .load(it)
                    .apply(RequestOptions().circleCrop())
                    .into(profileImageView)
            }
        }

        val drawableResource = when (profileData.peer) {
            in 0..19 -> R.drawable.profilecard_detail_peer0_19
            in 20..39 -> R.drawable.profilecard_detail_peer20_39
            in 40..59 -> R.drawable.profilecard_detail_peer40_59
            in 60..79 -> R.drawable.profilecard_detail_peer60_79
            in 80..100 -> R.drawable.profilecard_detail_peer80_100
            else -> R.drawable.profilecard_detail_peer40_59
        }
        peerImageView.setImageResource(drawableResource)
    }

    private fun decodeBase64(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: IllegalArgumentException) {
            e.printStackTrace()
            null
        }
    }

    private fun dpToPx(context: Context, dp: Int): Int {
        return (dp * context.resources.displayMetrics.density).toInt()
    }

//    private fun getRoundedBitmapWithBackground(bitmap: Bitmap, size: Int, backgroundColor: Int): Bitmap {
//        val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
//        val canvas = Canvas(output)
//        val paint = Paint().apply {
//            isAntiAlias = true
//            color = backgroundColor
//        }
//
//        // 배경색을 그립니다.
//        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)
//
//        // 이미지의 크기를 조정하여 가운데에 맞춥니다.
//        val shader = BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
//        paint.shader = shader
//
//        // 반지름은 50dp를 픽셀로 변환한 값이어야 합니다.
//        val radius = size / 2f
//        val center = size / 2f
//
//        // 이미지를 가운데에 맞춰서 원형으로 자릅니다.
//        canvas.drawCircle(center, center, radius / 2, paint)
//
//        return output
//    }

    private fun getUserInfo() {
        val cachedUserName = getCachedUserName()
        if (cachedUserName == null) {
            // 서버에서 정보를 가져올 때까지 기본 텍스트를 표시하지 않음
            // 필요한 경우 로딩 인디케이터를 표시할 수 있습니다.
            val token = getTokenFromSharedPreferences()
            val account = getAccountFromSharedPreferences()
            if (token != null && account != null) {
                val homeService = MainAuthService(this)
                homeService.setMainView(this)
                homeService.setUserNameCallback(this)
                homeService.getUserName(token, account)
            }
        } else {
            // 캐시된 사용자 이름이 있으면 UI 업데이트
            updateUserNameUI(cachedUserName)
        }
    }

    override fun onUserNameReceived(userName: String) {
        val cachedUserName = getCachedUserName()
        if (cachedUserName != userName) {
            cacheUserName(userName)
        }
        updateUserNameUI(userName)

    }

    private fun updateUserNameUI(userName: String) {
        val tvUserData = view?.findViewById<TextView>(R.id.tv_username_data)
        val tvUserData_2 = view?.findViewById<TextView>(R.id.tv_username_data_2)
        tvUserData?.text = "${userName}님,"
        tvUserData_2?.text = "${userName}님의 프로필"
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }

    private fun cacheProfileData(profileData: getProfilePreiviewData) {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPref?.edit()?.apply {
            putString("profilePhoto", profileData.photo)
            putInt("profilePeer", profileData.peer)
            apply()
        }
    }

    private fun cacheUserName(userName: String) {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        sharedPref?.edit()?.putString("cachedUserName", userName)?.apply()
    }

    private fun getCachedUserName(): String? {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("cachedUserName", null)
    }

    private fun getTokenFromSharedPreferences(): String? {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userToken", null)
    }

    // SharedPreferences에서 account 가져오는 함수
    private fun getAccountFromSharedPreferences(): String? {
        val sharedPref = activity?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getString("userAccount", null)
    }

    override fun onMainGetSuccess() {
        Log.d("USER/GET/SUCCESS", "유저정보 획득성공 콜백성공")
    }

    override fun onMainGetFailure() {
        Log.d("USER/GET/FAILURE", "유저정보 획득성공 콜백실패")
    }

    override fun onHomeGetSuccess(profileData: getProfilePreiviewData) {
        Log.d("HomeFragment", "유저프로필정보 획득성공 콜백성공")
        homeViewModel.setProfileData(profileData)
    }

    override fun onHomeGetFailure() {
        Log.e("HomeFragment", "Failed to get profile data")
    }

    // ProjectListView 인터페이스 구현
    override fun onProjectListSuccess(projects: List<ProjectResponse>) {
        homeViewModel.setProjectList(projects)
        Log.d("HomeFragment", "프로젝트 리스트 불러오는데 성공! 200")
    }

    override fun onProjectListEmpty() {
        homeViewModel.setProjectList(emptyList())
        Log.e("HomeFragment", "프로젝트 리스트가 비어있습니다.")
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

    //김대환 : db에서 nickname이 null이면 firstSetting 액티비티를 띄움
    fun checkNicknameIsEmpty() {

        val sharedPref = context?.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)

        val token = sharedPref?.getString("userToken", "")
        val memberId = sharedPref?.getInt("loginId", 0)

        if (!token.isNullOrEmpty() && memberId != null && memberId != 0) {
            val apiService = getRetrofit().create(getNicknameRequest::class.java)
            val call: Call<getNicknameData> = apiService.getNickname("Bearer $token", memberId)

            call.enqueue(object : Callback<getNicknameData> {
                override fun onResponse(
                    call: Call<getNicknameData>,
                    response: Response<getNicknameData>
                ) {
                    if (response.isSuccessful) {
                        val nicknameData = response.body()

                        if (nicknameData != null && nicknameData.nickname == null) {
                            // 닉네임이 비어있거나 널인 경우 처리
                            Log.d("NicknameCheck", "닉네임이 비어있음")

                            val intent = Intent(requireContext(), FirstSettingActivity::class.java)
                            startActivity(intent)
                        } else if (nicknameData != null) {
                            // 닉네임이 비어있지 않은 경우 처리
                            Log.d("NicknameCheck", "닉네임: ${nicknameData.nickname}")
                        } else {
                            // nicknameData가 널인 경우 처리
                            Log.e("NicknameCheck", "nicknameData is null")
                        }
                    } else {
                        Log.e("NicknameCheck", "Response unsuccessful")
                    }
                }

                override fun onFailure(call: Call<getNicknameData>, t: Throwable) {

                }
            })
        } else {
            // token이나 memberId가 유효하지 않은 경우
        }
    }
}