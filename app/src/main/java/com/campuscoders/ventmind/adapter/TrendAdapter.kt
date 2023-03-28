package com.campuscoders.ventmind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.TrendRowBinding
import com.campuscoders.ventmind.model.Trend
import com.campuscoders.ventmind.util.downloadFromUrl
import com.campuscoders.ventmind.util.placeHolderProgressBar

class TrendAdapter: RecyclerView.Adapter<TrendAdapter.MyViewHolder>() {

    private var trendList: MutableList<Trend> = arrayListOf()

    inner class MyViewHolder(val binding: TrendRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Trend, context: Context) {
            binding.textViewTrendName.text = item.trend_name
            binding.textViewTrendCount.text = item.trend_count.toString()
            binding.imageViewTrendIcon.downloadFromUrl(item.trend_avatar, placeHolderProgressBar(context))
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = TrendRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return trendList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val trend = trendList[position]
        holder.bind(trend,holder.binding.root.context)
    }

    fun updateList(list: MutableList<Trend>) {
        this.trendList = list
        notifyDataSetChanged()
    }
}