package com.gmlab.team_benew.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.gmlab.team_benew.R

class HomeBannerImageAdapter(private val imageList: List<Int>) : RecyclerView.Adapter<HomeBannerImageAdapter.HomeBannerViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeBannerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.home_banner, parent, false)
        return HomeBannerViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeBannerViewHolder, position: Int) {
        holder.imageView.setImageResource(imageList[position % imageList.size]) // 이미지가 제대로 설정되도록 인덱스 수정
    }
    inner class HomeBannerViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.iv_home_banner_image)
    }
    override fun getItemCount(): Int {
        return Integer.MAX_VALUE
    }
}
