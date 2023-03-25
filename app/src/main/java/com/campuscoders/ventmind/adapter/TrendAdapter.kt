package com.campuscoders.ventmind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.TrendRowBinding
import com.campuscoders.ventmind.model.Trend

class TrendAdapter: RecyclerView.Adapter<TrendAdapter.MyViewHolder>() {

    private var trendList: MutableList<Trend> = arrayListOf()

    inner class MyViewHolder(val binding: TrendRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Trend, pos: Int) {
            binding.textViewTrendName.text = item.trend_name
            binding.textViewTrendCount.text = item.trend_count.toString()
            // avatar yükleme - glide
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
        holder.bind(trend,position)
    }

    fun updateList(list: MutableList<Trend>) {
        this.trendList = list
        notifyDataSetChanged()
    }
}