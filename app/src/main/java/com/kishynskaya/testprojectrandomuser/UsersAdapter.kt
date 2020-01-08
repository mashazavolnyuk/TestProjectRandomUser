package com.kishynskaya.testprojectrandomuser

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import kotlinx.android.synthetic.main.item_user.view.*

class UsersAdapter : PagedListAdapter<Result, UsersAdapter.UserViewHolder>(UsersDiffCallback) {

    var onclickListenerResult: OnClickResult? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }


    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    fun setOnclickListener(onclickListenerResult: OnClickResult) {
        this.onclickListenerResult = onclickListenerResult
    }

     inner class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(result: Result) {
            itemView.setOnClickListener {
                onclickListenerResult?.onClickResult(result)
            }
            val textInfo = result.name.run {
                "$first $last"
            }
            itemView.textViewInfoUser.text = textInfo
            Glide
                .with(itemView)
                .load(result.picture.medium)
                .error(R.drawable.ic_image_error)
                .transform(CircleCrop())
                .into(itemView.imageViewLogo)
        }
    }

    interface OnClickResult {
        fun onClickResult(result: Result)
    }

    companion object {
        val UsersDiffCallback = object : DiffUtil.ItemCallback<Result>() {
            override fun areItemsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Result, newItem: Result): Boolean {
                return oldItem == newItem
            }
        }
    }
}