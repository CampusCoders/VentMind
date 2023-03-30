package com.campuscoders.ventmind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.AvatarsBinding
import com.campuscoders.ventmind.model.Avatar
import com.campuscoders.ventmind.util.downloadFromUrl
import com.campuscoders.ventmind.util.placeHolderProgressBar

class AvatarAdapter(
    val onAvatarClickListener: (String) -> Unit
): RecyclerView.Adapter<AvatarAdapter.MyAvatarViewHolder>() {

    private var avatarList: MutableList<Avatar> = arrayListOf()

    inner class MyAvatarViewHolder(val binding: AvatarsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Avatar, context: Context) {
            binding.textViewAvatarPrice.text = item.avatar_price.toString()
            binding.imageViewSingleAvatar.downloadFromUrl(item.avatar_source, placeHolderProgressBar(context))
            binding.linearSingleAvatar.setOnClickListener {
                onAvatarClickListener.invoke(item.avatar_source.toString())
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAvatarViewHolder {
        val itemView = AvatarsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyAvatarViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return avatarList.size
    }

    override fun onBindViewHolder(holder: MyAvatarViewHolder, position: Int) {
        val item = avatarList[position]
        holder.bind(item, holder.binding.root.context)
    }

    fun updateList(list: MutableList<Avatar>) {
        this.avatarList = list
        notifyDataSetChanged()
    }
}