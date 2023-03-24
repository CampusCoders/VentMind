package com.campuscoders.ventmind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.PostFeedBinding
import com.campuscoders.ventmind.model.PostFeed
import java.text.SimpleDateFormat

class FeedAdapter(
    val avatarOnItemClickListener: (String) -> Unit,
    val usernameOnItemClickListener: (String) -> Unit,
    val likeOnItemClickListener: (String) -> Unit,
    val commentOnItemClickListener: (String) -> Unit,
): RecyclerView.Adapter<FeedAdapter.MyViewHolder>() {

    val sdf = SimpleDateFormat("dd MMM yyyy")

    private var postFeedList: MutableList<PostFeed> = arrayListOf()

    inner class MyViewHolder(val binding: PostFeedBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: PostFeed) {
            binding.textViewFeedPostUsername.text = item.post_nick
            binding.textViewFeedPostTag.text = item.post_tag
            binding.textViewFeedLikeCount.text = item.post_like_count.toString()
            binding.textViewFeedCommentCount.text = item.post_comment_count.toString()
            binding.textViewFeedPostContent.text = item.post_content
            binding.textViewFeedPostDate.text = item.created_at?.let { sdf.format(it) }
            // clickListeners:
            binding.imageViewFeedPostAvatar.setOnClickListener { avatarOnItemClickListener.invoke(item.post_user_id ?: "") }
            binding.textViewFeedPostUsername.setOnClickListener { usernameOnItemClickListener.invoke(item.post_user_id ?: "") }
            binding.imageViewFeedLike.setOnClickListener { likeOnItemClickListener.invoke(item.post_id ?: "") }
            binding.imageViewFeedComments.setOnClickListener { commentOnItemClickListener.invoke(item.post_id ?: "") }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = PostFeedBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return postFeedList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val item = postFeedList[position]
        holder.bind(item)
    }

    fun updateList(list: MutableList<PostFeed>) {
        this.postFeedList = list
        notifyDataSetChanged()
    }
}