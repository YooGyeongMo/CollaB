package com.gmlab.team_benew.project.projectpostdetail

import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.setFragmentResult
import androidx.navigation.fragment.findNavController
import com.gmlab.team_benew.databinding.FragmentMyprojectPostBinding

class ProjectPostDetailFragment : Fragment(), ProjectPostDetailView {

    private var _binding: FragmentMyprojectPostBinding? = null
    private val binding get() = _binding!!
    private lateinit var postService: ProjectDetailPostService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentMyprojectPostBinding.inflate(inflater, container, false)
        postService = ProjectDetailPostService(requireContext(), this)
        setupTextWatchers()
        binding.btnNewProject.setOnClickListener {
            if (validateInputs()) {
                val userId = getIdFromSharedPreferences()
                if (userId != null) {
                    val projectRequestDto = PostProjectDetailRequest(
                        chatroomId = null,
                        projectIntroduction = binding.etInputMyNewProjectExplainIntroductionData.text.toString(),
                        projectName = binding.etInputMyNewProjectTitleData.text.toString(),
                        projectOneLineIntroduction = binding.etInputMyNewProjectSummaryData.text.toString(),
                        userId = userId,
                        projectManager = userId.toLong()
                    )
                    postService.postProject(projectRequestDto)
                } else {
                    Log.e("ProjectPostDetail", "User ID를 찾을 수 없습니다.")
                }
            }
        }
        return binding.root
    }

    private fun setupTextWatchers() {
        binding.etInputMyNewProjectTitleData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                binding.tvTitleCount.text = "$inputLength/15"

                if (inputLength > 15) {
                    binding.etInputMyNewProjectTitleData.error = "15자를 넘을 수 없습니다."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputs() // 추가된 코드: 각 필드가 변경될 때마다 입력 유효성을 검사
            }
        })

        binding.etInputMyNewProjectSummaryData.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                binding.tvSummaryCount.text = "$inputLength/30"

                if (inputLength > 30) {
                    binding.etInputMyNewProjectSummaryData.error = "30자를 넘을 수 없습니다."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputs() // 추가된 코드: 각 필드가 변경될 때마다 입력 유효성을 검사
            }
        })

        binding.etInputMyNewProjectExplainIntroductionData.addTextChangedListener(object :
            TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val inputLength = s?.length ?: 0
                binding.tvExplainCount.text = "$inputLength/500"

                if (inputLength > 500) {
                    binding.etInputMyNewProjectExplainIntroductionData.error = "500자를 넘을 수 없습니다."
                }
            }

            override fun afterTextChanged(s: Editable?) {
                validateInputs() // 추가된 코드: 각 필드가 변경될 때마다 입력 유효성을 검사
            }
        })
    }

    private fun validateInputs(): Boolean {
        var isValid = true
        val titleText = binding.etInputMyNewProjectTitleData.text.toString()
        val summaryText = binding.etInputMyNewProjectSummaryData.text.toString()
        val explainText = binding.etInputMyNewProjectExplainIntroductionData.text.toString()

        if (titleText.isBlank() || titleText.length > 15) {
            binding.etInputMyNewProjectTitleData.error =
                if (titleText.isBlank()) "필수 입력 항목입니다." else "15자를 넘을 수 없습니다."
            isValid = false
        }

        if (summaryText.isBlank() || summaryText.length > 30) {
            binding.etInputMyNewProjectSummaryData.error =
                if (summaryText.isBlank()) "필수 입력 항목입니다." else "30자를 넘을 수 없습니다."
            isValid = false
        }

        if (explainText.isBlank() || explainText.length > 500) {
            binding.etInputMyNewProjectExplainIntroductionData.error =
                if (explainText.isBlank()) "필수 입력 항목입니다." else "500자를 넘을 수 없습니다."
            isValid = false
        }

        return isValid
    }
    private fun getIdFromSharedPreferences(): Int? {
        val sharedPref = requireContext().getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref?.getInt("loginId", -1).takeIf { it != -1 }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onProjectPostSuccess() {
        Log.d("ProjectPostDetail", "PostSucess!!")
        // 결과 설정
        setFragmentResult("projectPostResult", bundleOf("isSuccess" to true))
        // 이전 프래그먼트로 이동
        findNavController().popBackStack()
    }

    override fun onProjectPostFailure(code: Int, message: String) {
        // 예외 처리
        when (code) {
            401 -> Log.e("ProjectPostDetail", "Unauthorized: $message")
            403 -> Log.e("ProjectPostDetail", "Forbidden: $message")
            404 -> Log.e("ProjectPostDetail", "Not Found: $message")
            else -> {
                Log.e("ProjectPostDetail", "Error $code: $message")
                // 결과 설정
                setFragmentResult("projectPostResult", bundleOf("isSuccess" to true))
                // 이전 프래그먼트로 이동
                findNavController().popBackStack()
            }
        }
    }
}