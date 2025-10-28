package ie.setu.videoapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class VideoAdapter(
    private val items: MutableList<VideoModel>,
    private val onItemClick: (VideoModel) -> Unit,
    private val onEditClick: (Int) -> Unit,
    private val onDeleteClick: (Int) -> Unit
) : RecyclerView.Adapter<VideoAdapter.VideoViewHolder>() {



    inner class VideoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.title)
        val source: TextView = itemView.findViewById(R.id.source)
        val thumb: ImageView = itemView.findViewById(R.id.thumb)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_video, parent, false)
        return VideoViewHolder(view)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        val video = items[position]
        holder.title.text = video.title
        holder.source.text = video.url

        if (video.thumbnailUrl.isNotEmpty()) {
            Glide.with(holder.itemView.context)
                .load(video.thumbnailUrl)
                .placeholder(android.R.color.darker_gray)
                .into(holder.thumb)
        } else {
            holder.thumb.setImageResource(android.R.color.darker_gray)
        }

        holder.itemView.setOnClickListener {
            onItemClick(video)
        }

        holder.itemView.setOnLongClickListener {
            val popup = android.widget.PopupMenu(holder.itemView.context, holder.itemView)
            popup.menuInflater.inflate(R.menu.video_item_menu, popup.menu)
            popup.setOnMenuItemClickListener { item ->
                when (item.itemId) {
                    R.id.action_edit -> {
                        onEditClick(position)
                        true
                    }
                    R.id.action_delete -> {
                        onDeleteClick(position)
                        true
                    }
                    else -> false
                }
            }
            popup.show()
            true
        }

    }


    override fun getItemCount(): Int = items.size
}
