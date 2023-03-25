package com.campuscoders.ventmind.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.campuscoders.ventmind.databinding.UserRowBinding
import com.campuscoders.ventmind.model.User

class UserListAdapter(
    val avatarOnItemClickListener: (String) -> Unit,
    val usernameOnItemClickListener: (String) -> Unit
): RecyclerView.Adapter<UserListAdapter.MyViewHolder>() {

    private var userList: MutableList<User> = arrayListOf()

    inner class MyViewHolder(val binding: UserRowBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(item: User, pos: Int) {
            binding.textViewUserRank.text = pos.toString()
            binding.textViewUserListUserName.text = item.user_nick
            binding.textViewUserListScore.text = item.user_score.toString()
            // avatar glide ile verilecek

            // clickListeners
            //binding.imageViewUserListAvatar.setOnClickListener { avatarOnItemClickListener.invoke(item.user_id) }
            //binding.textViewUserListUserName.setOnClickListener { usernameOnItemClickListener.invoke(item.user_id) }
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
        holder.bind(userItem,position)
    }

    fun updateList(list: MutableList<User>) {
        this.userList = list
        notifyDataSetChanged()
    }
}