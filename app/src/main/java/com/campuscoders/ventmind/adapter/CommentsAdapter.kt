package com.campuscoders.ventmind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.PostCommentsBinding
import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.show

class CommentsAdapter(
    val onCommentClickListener: (String,String) -> Unit
): RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    private var commentList: MutableList<Comment> = arrayListOf()

    inner class MyViewHolder(val binding: PostCommentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment) {
            binding.textViewSubCommentsUsername.text = item.comment_user_nick
            binding.textViewSubCommentsContent.text = item.comment_content
            // avatar resmi glide ile
            if(item.comment_award != null) {
                if(item.comment_award) {
                    // ödül verilmiş
                    binding.imageViewAward.show()
                } else {
                    binding.imageViewAward.hide()
                }
            }
            // clicklisteners:
            binding.linearSubComment.setOnClickListener {
                // comment'e tıklanılırsa userId ve postId döner
                onCommentClickListener.invoke(
                    item.comment_user_id.toString(),
                    item.comment_rootpost_id.toString()
                )
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = PostCommentsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return commentList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val comment = commentList[position]
        holder.bind(comment)
    }

    fun updateList(list: MutableList<Comment>) {
        this.commentList = list
        notifyDataSetChanged()
    }
}