package com.gmlab.team_benew.matching

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.gmlab.team_benew.R
import com.yuyakaido.android.cardstackview.CardStackLayoutManager
import com.yuyakaido.android.cardstackview.CardStackListener
import com.yuyakaido.android.cardstackview.CardStackView
import com.yuyakaido.android.cardstackview.Direction

class MatchingFragment : Fragment(), MatchingPostView, MatchingAlarmsPostView {
    private lateinit var matchingService: MatchingService
    private lateinit var cardStackAdapter: CardStackAdapter
    private lateinit var manager: CardStackLayoutManager
    private lateinit var cardStackView: CardStackView
    private lateinit var progressBar: ProgressBar

    private val matchingViewModel: MatchingViewModel by activityViewModels()
    private val sharedViewModel: SharedViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_matching, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressBar = view.findViewById(R.id.pg_matchingCheck_progressBar)
        matchingService = MatchingService.getInstance(requireContext())
        matchingService.setMatchingPostView(this)
        matchingService.setMatchingAlarmsPostView(this)

        cardStackView = view.findViewById(R.id.cardstackView)
        manager = CardStackLayoutManager(requireContext(), object : CardStackListener {
            override fun onCardDragging(direction: Direction?, ratio: Float) {}

            override fun onCardSwiped(direction: Direction?) {
                val currentPosition = manager.topPosition - 1

                val currentMatchId = matchingViewModel.matchingId.value?.get(currentPosition)
                val currentProfile = matchingViewModel.matchingProfiles.value?.get(currentPosition)

                if (direction == Direction.Right) {
                    currentProfile?.let { handleRightSwipe(it, currentMatchId ?: 0L) }
                } else if (direction == Direction.Left) {
                    currentMatchId?.let { handleLeftSwipe(it) }
                }

                matchingViewModel.removeProfileAt(currentPosition)
                matchingViewModel.incrementUserCount()

                if (matchingViewModel.matchingProfiles.value?.size ?: 0 <= 5 && !(matchingViewModel.isFetching.value ?: false)) {
                    fetchMoreProfiles()
                }
            }

            override fun onCardRewound() {}

            override fun onCardCanceled() {
                Log.d("CARDCANCELED", "카드 놓침")
            }

            override fun onCardAppeared(view: View?, position: Int) {}

            override fun onCardDisappeared(view: View?, position: Int) {}
        })

        observeViewModel()
        if (savedInstanceState == null) {
            fetchMatchingProfiles()
        }
    }

    private fun observeViewModel() {
        matchingViewModel.matchingProfiles.observe(viewLifecycleOwner, Observer { profiles ->
            updateUI(profiles)
        })

        matchingViewModel.isFetching.observe(viewLifecycleOwner, Observer { isFetching ->
            if (isFetching) {
                showProgressBar()
            } else {
                hideProgressBar()
            }
        })
    }

    private fun fetchMatchingProfiles() {
        val userId = getIdFromSharedPreferences(requireContext()) ?: return
        matchingViewModel.fetchMatchingProfiles(matchingService, userId) { errorMessage ->
            showRetryDialog(errorMessage)
        }
    }

    private fun fetchMoreProfiles() {
        val userId = getIdFromSharedPreferences(requireContext()) ?: return
        matchingViewModel.fetchMatchingProfiles(matchingService, userId) { errorMessage ->
            showRetryDialog(errorMessage)
        }
    }

    private fun handleRightSwipe(currentProfile: Profile, currentMatchId: Long) {
        val receiverId = currentProfile.id.toLong()
        val myUserId = getIdFromSharedPreferences(requireContext())?.toLong() ?: return
        val myName = getNameFromSharedPreferences(requireContext()) ?: return
        val project = sharedViewModel.selectedProject.value ?: return // 프로젝트 정보를 가져옴

        val message = "$myName 님이, 프로젝트 ${project.projectName}, 번호 ${project.projectId}에 대한 요청을  보냈습니다."
        val matchingAlarmRequest = MatchingAlarmRequest(message, receiverId, myUserId)

        matchingService.sendMatchingAlrams(matchingAlarmRequest,
            onResponse = { showSuccessDialog("프로젝트 요청을 성공적으로 보냈습니다.") },
            onFailure = { showRetryDialog("프로젝트 요청을 보내지 못했습니다.") }
        )

//        matchingService.likeMatch(currentMatchId, project.projectId) { response ->
////            Toast.makeText(context, "$currentMatchId 매칭에 '좋아요'를 보냈습니다.", Toast.LENGTH_SHORT).show()
//        }
    }

    private fun handleLeftSwipe(currentMatchId: Long) {
//        matchingService.disLikeMatch(currentMatchId,
//            onResponse = {
////                Toast.makeText(context, "$currentMatchId 매칭에 '싫어요'를 보냈습니다.", Toast.LENGTH_SHORT).show()
//            },
//            onFailure = {
//                Toast.makeText(context, "매칭에 '싫어요'를 보내지 못했습니다.", Toast.LENGTH_SHORT).show()
//                Log.e("MatchingFragment", "disLikeMatch failed", it)
//            }
//        )
    }

    private fun updateUI(profiles: List<Profile>) {
        if (!::cardStackAdapter.isInitialized) {
            cardStackAdapter = CardStackAdapter(requireContext(), profiles)
            cardStackView.layoutManager = manager
            cardStackView.adapter = cardStackAdapter
        } else {
            cardStackAdapter.items = profiles
            cardStackAdapter.notifyDataSetChanged()
        }
    }

    private fun showSuccessDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("성공")
            setMessage(message)
            setPositiveButton("확인") { dialog, _ -> dialog.dismiss() }
            create().show()
        }
    }

    private fun showRetryDialog(message: String) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle("오류")
            setMessage(message)
            setPositiveButton("확인") { dialog, _ ->
                dialog.dismiss()
                fetchMoreProfiles()  // 확인 버튼 클릭 시 다시 요청
            }
            create().show()
        }
    }

    private fun getNameFromSharedPreferences(context: Context): String? {
        val shardPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return shardPref.getString("cachedUserName", null)
    }

    private fun getIdFromSharedPreferences(context: Context): Int? {
        val sharedPref = context.getSharedPreferences("MyAppPrefs", Context.MODE_PRIVATE)
        return sharedPref.getInt("loginId", -1).takeIf { it != -1 }
    }

    private fun showProgressBar() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        progressBar.visibility = View.GONE
    }

    override fun onMatchingPostSuccess() {
        Log.d("MATCHING/POST/SUCCESS", "유저매칭POST 성공")
    }

    override fun onMatchingPostFailure() {
        Log.e("MATCHING/POST/FAILURE", "유저매칭POST 실패")
    }

    override fun onMatchingLikePatchSuccess() {
        Log.d("MATCHINGLIKE/PATCH/SUCCESS", "유저매칭 좋아요 성공~")
    }

    override fun onMatchingLikePatchFailure() {
        Log.e("MATCHINGLIKE/PATCH/FAILURE", "유저매칭 좋아요 실패 ㅠㅠ")
    }

    override fun onMatchingUnLikePatchSuccess() {
        Log.d("MATCHINGUNLIKE/PATCH/SUCCESS", "유저매칭 싫어요 성공~")
    }

    override fun onMatchingUnLikePatchFailure() {
        Log.e("MATCHINGLIKE/PATCH/FAILURE", "유저매칭 싫어요 실패 ㅠㅠ ")
    }

    override fun onMatchingAlarmsSuccess() {
        Log.d("MATCHINGALARMS/POST/SUCCESS", "유저매칭 알람 성공!")
    }

    override fun onMatchingAlarmsFailure() {
        Log.e("MATCHINGALARMS/POST/FAILURE", "유저매칭 알람 실패!")
    }

    override fun onMatchingRequestFailure(errorMessage: String) {
        showRetryDialog(errorMessage)
    }
}
