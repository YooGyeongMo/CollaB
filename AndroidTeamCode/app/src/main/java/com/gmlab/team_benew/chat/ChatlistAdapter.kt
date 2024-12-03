package com.gmlab.team_benew.chat

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import android.util.Log
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R
import com.gmlab.team_benew.chat.retrofit.chatdata
import com.gmlab.team_benew.chat.socket.ChatFragment
import com.gmlab.team_benew.databinding.ChatRecyclerItemBinding
import com.gmlab.team_benew.main.MainActivity

//context=ChatlistAdapter 클래스의 인스턴스를 생성할 때, context 매개변수를 통해 Android 애플리케이션 환경에 액세스할 수 있
class ChatlistAdapter(private val navController: NavController) : RecyclerView.Adapter<Holder>()  {
    //데이터들을 저장하는 변수
    var modelList=mutableListOf<chatdata>()


    //뷰홀더에 쓰일 레이아웃 인플레이트
    //뷰그룹의 context써서
    //뷰홀더 새로 만들때마다 이거 호출
    //내용은 안채우고 데이터는 따로 바인딩 해야함
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding= ChatRecyclerItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return Holder(binding)

    }

    //뷰홀더에서 데이터 묶는 함수 실행 됨
    //뷰홀더를 데이터와 연결할때 호출함
    override fun onBindViewHolder(holder: Holder, position: Int) {
        val chatItem = modelList[position]
        holder.bind(chatItem)
        holder.itemView.setOnClickListener {
            val bundle = Bundle().apply {
                putString("roomId", chatItem.roomId)
            }
            navController.navigate(R.id.action_chatListFragment_to_chatRoomFragment, bundle)
        }

    }

    override fun getItemCount(): Int {
        return modelList.size
        //return chatList.count()
    }


}
class Holder(val binding:ChatRecyclerItemBinding): RecyclerView.ViewHolder(binding.root){

    val chatroomtext=binding.chatRoomName
    //val chatRoomId=binding.roomId//roomId뜨는지 확인해보려고 만든 텍스트 뷰
    fun bind(myModel: chatdata){//여기서 텍스트(응답 값으로 받은)를 연결해야함
        //binding.userName.text=myModel.name
        //binding.userName.text="${myModel.roomId}"
        //여기서 userName은 카드뷰 xml의 textview임
        //일단 데이터랑 뷰홀더랑 연결함

        //chatroomtext.text=myModel.roomId
        chatroomtext.text=myModel.name
        //chatRoomId.text=myModel.roomId
    }
}