package br.com.rettiwt.activity.home

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import br.com.rettiwt.R
import br.com.rettiwt.changeDateFormat
import br.com.rettiwt.models.HomeItemModel
import br.com.rettiwt.setVisible
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import kotlinx.android.synthetic.main.item_home.view.*

class HomeAdapter(private val onClickListener: OnClickListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    interface OnClickListener {
        fun onClickStar(id: String?)
        fun onClickRettiwt(id: String?)
    }

    var list: List<HomeItemModel> = listOf()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_home, parent, false)
        return ItemViewHolder(itemView)
    }

    override fun getItemCount() = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ItemViewHolder) {
            holder.bind(list[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: HomeItemModel) {
            itemView.apply {
                itemHomeRettiwtIv.setVisible(item.originalPoster != null)
                itemHomeRettiwtNameTv.setVisible(item.originalPoster != null)
                itemHomeRettiwtNameTv.text = item.originalPoster
                itemHomeBodyTv.setVisible(!item.text.isNullOrBlank())
                itemHomeBodyTv.text = item.text
                itemHomeNameTv.text = item.user
                itemHomeRettiwtsTv.text = "${item.rettiwts ?: 0}"
                itemHomeStarsTv.text = "${item.stars ?: 0}"
                itemHomeBodyDateTv.text = item.date?.changeDateFormat("yyyy-MM-dd'T'HH:mm:ssss", "dd 'de' MMMM HH:mm")
                itemHomeCard.setVisible(item.picture != null)
                Glide.with(this).load(item.picture)
                        .apply(RequestOptions().placeholder(R.drawable.bg_placeholder_photo)).into(itemHomeIv)
                itemHomeStarsLl.setOnClickListener {
                    onClickListener.onClickStar(item.id)
                }
                itemHomeRettiwtLl.setOnClickListener {
                    onClickListener.onClickRettiwt(item.id)
                }
            }
        }
    }
}