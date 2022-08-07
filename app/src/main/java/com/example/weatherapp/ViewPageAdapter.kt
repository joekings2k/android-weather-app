package com.example.weatherapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPageAdapter (private val images:List<Int>):RecyclerView.Adapter<ViewPageAdapter.ViewPagerViewHolder>(){


    inner class ViewPagerViewHolder(itemView:View):RecyclerView.ViewHolder(itemView) {

        val imageView:ImageView =itemView.findViewById(R.id.ivImage)


    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPageAdapter.ViewPagerViewHolder {
        val view =LayoutInflater.from(parent.context).inflate(R.layout.item,parent,false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewPageAdapter.ViewPagerViewHolder, position: Int) {
        val curImages = images[position]
        holder.imageView.setImageResource(curImages)

    }



    override fun getItemCount(): Int {
        return images.size
    }

}