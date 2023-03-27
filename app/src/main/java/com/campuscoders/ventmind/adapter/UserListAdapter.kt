package com.campuscoders.ventmind.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.UserRowBinding
import com.campuscoders.ventmind.model.User
import com.campuscoders.ventmind.util.downloadFromUrl
import com.campuscoders.ventmind.util.placeHolderProgressBar

class UserListAdapter(
    val avatarOnItemClickListener: (String) -> Unit,
    val usernameOnItemClickListener: (String) -> Unit
): RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    private var userList: MutableList<User> = arrayListOf()

    inner class MyViewHolder(val binding: UserRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, pos: Int, context: Context) {
            binding.textViewUserRank.text = pos.plus(1).toString()
            binding.textViewUserListUserName.text = item.user_nick
            binding.textViewUserListScore.text = item.user_score.toString()
            binding.imageViewUserListAvatar.downloadFromUrl(item.user_avatar, placeHolderProgressBar(context))

            // clickListeners
            binding.imageViewUserListAvatar.setOnClickListener { avatarOnItemClickListener.invoke(item.user_id.toString()) }
            binding.textViewUserListUserName.setOnClickListener { usernameOnItemClickListener.invoke(item.user_id.toString()) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = UserRowBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MyViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val userItem = userList[position]
        holder.bind(userItem,position,holder.binding.root.context)
    }

    fun updateList(list: MutableList<User>) {
        this.userList = list
        notifyDataSetChanged()
    }
}