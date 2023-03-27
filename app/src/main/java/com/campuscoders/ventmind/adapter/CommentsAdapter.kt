package com.campuscoders.ventmind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.PostCommentsBinding
import com.campuscoders.ventmind.model.Comment
import com.campuscoders.ventmind.util.downloadFromUrl
import com.campuscoders.ventmind.util.hide
import com.campuscoders.ventmind.util.placeHolderProgressBar
import com.campuscoders.ventmind.util.show

class CommentsAdapter(
    val onCommentClickListener: (String,String) -> Unit,
): RecyclerView.Adapter<CommentsAdapter.MyViewHolder>() {

    private var commentList: MutableList<Comment> = arrayListOf()

    private var control: MutableList<Boolean> = arrayListOf()

    inner class MyViewHolder(val binding: PostCommentsBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Comment, context: Context) {
            binding.textViewSubCommentsUsername.text = item.comment_user_nick
            binding.textViewSubCommentsContent.text = item.comment_content
            binding.imageViewSubCommentsAvatar.downloadFromUrl(item.comment_avatar, placeHolderProgressBar(context))
            if(item.comment_award != null) {
                if(item.comment_award!!) {
                    binding.imageViewAward.show()
                } else {
                    binding.imageViewAward.hide()
                }
            }

            println(" BİND İÇİNDEYİM")
            if(control.isNotEmpty()) {
                println("${control.first()}")
                if(control.first()) {
                    binding.linearSubComment.setOnClickListener {
                        // comment'e tıklanılırsa userId ve postId döner
                        println("1")
                        onCommentClickListener.invoke(
                            item.comment_id.toString(),
                            item.comment_rootpost_id.toString()
                        )

                        if(binding.imageViewAward.visibility == View.VISIBLE) {
                            println("2")
                            binding.imageViewAward.hide()
                        } else {
                            binding.imageViewAward.show()
                            println("3")
                        }
                    }
                }
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
        holder.bind(comment, holder.binding.root.context)
    }

    fun updateList(list: MutableList<Comment>) {
        this.commentList = list
        notifyDataSetChanged()
    }

    fun updateControl(list: MutableList<Boolean>) {
        this.control = list
        notifyDataSetChanged()
        println("updateControl: ${this.control.first()}")
    }
}