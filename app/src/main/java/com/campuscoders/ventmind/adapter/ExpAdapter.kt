package com.campuscoders.ventmind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.PostExperienceBinding
import com.campuscoders.ventmind.model.PostExp
import com.campuscoders.ventmind.util.downloadFromUrl
import com.campuscoders.ventmind.util.placeHolderProgressBar
import java.text.SimpleDateFormat

class ExpAdapter(
    val avatarOnItemClickListener: (String) -> Unit,
    val usernameOnItemClickListener: (String) -> Unit,
    val likeOnItemClickListener: (String) -> Unit,
    val dislikeOnItemClickListener: (String) -> Unit,
): RecyclerView.Adapter<ExpAdapter.ExpViewHolder>() {

    val sdf = SimpleDateFormat("dd MMM yyyy")
    private var postExpList: MutableList<PostExp> = arrayListOf()

    inner class ExpViewHolder(val binding: PostExperienceBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostExp, context: Context) {
            binding.textViewExperiencePostUsername.text = item.post_nick
            binding.textViewPostExperienceTag.text = item.post_tag
            binding.textViewExperienceLikeCount.text = item.post_like_count.toString()
            binding.textViewExperienceDislikeCount.text = item.post_dislike_count.toString()
            binding.textViewExperiencePostContent.text = item.post_content
            binding.textViewExperiencePostDate.text = item.created_at?.let { sdf.format(it) }
            binding.imageViewExperiencePostAvatar.downloadFromUrl(item.post_avatar, placeHolderProgressBar(context))

            binding.imageViewExperiencePostAvatar.setOnClickListener { avatarOnItemClickListener.invoke(item.post_user_id ?: "") }
            binding.textViewExperiencePostUsername.setOnClickListener { usernameOnItemClickListener.invoke(item.post_user_id ?: "") }
            binding.imageViewExperienceLike.setOnClickListener { likeOnItemClickListener.invoke(item.post_id ?: "") }
            binding.imageViewExperienceDislike.setOnClickListener { dislikeOnItemClickListener.invoke(item.post_id ?: "") }
        }
    }

    override fun onBindViewHolder(holder: ExpViewHolder, position: Int) {
        val item = postExpList[position]
        holder.bind(item, holder.binding.root.context)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ExpViewHolder {
        val itemView = PostExperienceBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ExpViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postExpList.size
    }

    fun updateList(list: MutableList<PostExp>) {
        this.postExpList = list
        notifyDataSetChanged()
    }
}