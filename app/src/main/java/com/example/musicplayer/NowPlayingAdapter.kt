package com.example.musicplayer

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.musicplayer.databinding.PlaylistRecyclerViewBinding

class NowPlayingAdapter: RecyclerView.Adapter<NowPlayingAdapter.ViewHolder>(

) {
    inner class ViewHolder(val binding: PlaylistRecyclerViewBinding): RecyclerView.ViewHolder(binding.root) {

            fun bind(tracksItem: TracksItem) = with(itemView){
                itemView.setOnClickListener {
                    val directions = NowPlayingFragmentDirections.actionNowPlayingFragment2ToPlayListFragment(tracksItem)
                    itemView.findNavController().navigate(directions)

                }
                user.text = tracksItem.name
                users.text = tracksItem.artist_name
                Glide.with(binding.root.context)
                    .load(tracksItem.artwork)
                    .into(image)
            }
        var user: TextView = binding.eminem
        var users : TextView = binding.starBoy
        var image: ImageView = binding.shapeableImageView


    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = PlaylistRecyclerViewBinding.inflate(LayoutInflater.from(parent.context), parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(differ.currentList[position])

    }

    override fun getItemCount(): Int {
        return differ.currentList.size
    }


    private val differCallBack = object : DiffUtil.ItemCallback<TracksItem>() {
        override fun areItemsTheSame(oldItem: TracksItem, newItem: TracksItem): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: TracksItem, newItem: TracksItem): Boolean {
            return oldItem == newItem
        }
    }

    var differ = AsyncListDiffer(this, differCallBack)
}
