package ie.setu.videoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.videoapp.databinding.ActivityMainBinding
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections


class MainActivity : AppCompatActivity() {

    private val videos = mutableListOf<VideoModel>()
    private lateinit var adapter: VideoAdapter
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // default sample video
        videos.add(
            VideoModel(
                "Never Gonna Give You Up",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
            )
        )

        adapter = VideoAdapter(
            videos,
            onItemClick = { video ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
                startActivity(intent)
            },
            onEditClick = { position ->
                showEditDialog(videos[position], position)
            },
            onDeleteClick = { position ->
                val video = videos[position]
                val builder = androidx.appcompat.app.AlertDialog.Builder(this)
                builder.setTitle("Delete video?")
                builder.setMessage("Are you sure you want to delete \"${video.title}\"?")
                builder.setPositiveButton("Delete") { _, _ ->
                    videos.removeAt(position)
                    adapter.notifyItemRemoved(position)
                    val snackbar = com.google.android.material.snackbar.Snackbar.make(
                        binding.root,
                        "Video deleted",
                        com.google.android.material.snackbar.Snackbar.LENGTH_LONG
                    )
                    snackbar.setAction("UNDO") {
                        videos.add(position, video)
                        adapter.notifyItemInserted(position)
                    }
                    snackbar.show()
                }
                builder.setNegativeButton("Cancel", null)
                builder.show()
            }
        )

        binding.videoRecycler.layoutManager = LinearLayoutManager(this)
        binding.videoRecycler.adapter = adapter

        // Enable drag-and-drop reordering
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                val fromPosition = viewHolder.adapterPosition
                val toPosition = target.adapterPosition

                // Swap the videos in the list
                Collections.swap(videos, fromPosition, toPosition)
                adapter.notifyItemMoved(fromPosition, toPosition)
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // No swipe action for now
            }

            override fun isLongPressDragEnabled(): Boolean {
                // Allow drag by holding down the item
                return true
            }

            override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
                super.onSelectedChanged(viewHolder, actionState)
                // Optional lift effect while dragging
                if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                    viewHolder?.itemView?.alpha = 0.7f
                    viewHolder?.itemView?.scaleX = 1.05f
                    viewHolder?.itemView?.scaleY = 1.05f
                }
            }

            override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
                super.clearView(recyclerView, viewHolder)
                // Reset view appearance after drag
                viewHolder.itemView.alpha = 1.0f
                viewHolder.itemView.scaleX = 1.0f
                viewHolder.itemView.scaleY = 1.0f
            }
        }

// Attach it to the RecyclerView
        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(binding.videoRecycler)


        // add new video
        binding.addFab.setOnClickListener {
            val builder = androidx.appcompat.app.AlertDialog.Builder(this)
            builder.setTitle("Add new video")

            val inputTitle = android.widget.EditText(this)
            inputTitle.hint = "Video title"

            val inputUrl = android.widget.EditText(this)
            inputUrl.hint = "Video URL"

            val layout = android.widget.LinearLayout(this)
            layout.orientation = android.widget.LinearLayout.VERTICAL
            layout.setPadding(50, 40, 50, 10)
            layout.addView(inputTitle)
            layout.addView(inputUrl)

            builder.setView(layout)

            builder.setPositiveButton("Add") { _, _ ->
                val title = inputTitle.text.toString().trim()
                val url = inputUrl.text.toString().trim()

                if (title.isNotEmpty() && url.isNotEmpty()) {
                    val videoId = when {
                        url.contains("youtube.com/watch?v=") ->
                            url.substringAfter("v=").substringBefore("&")
                        url.contains("youtu.be/") ->
                            url.substringAfter("youtu.be/").substringBefore("?")
                        else -> ""
                    }

                    val thumb = if (videoId.isNotEmpty()) {
                        "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"
                    } else ""

                    videos.add(VideoModel(title, url, thumb))
                    adapter.notifyItemInserted(videos.size - 1)
                }
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }

    private fun showEditDialog(video: VideoModel, position: Int) {
        val builder = androidx.appcompat.app.AlertDialog.Builder(this)
        builder.setTitle("Edit video")

        val inputTitle = android.widget.EditText(this)
        inputTitle.setText(video.title)

        val inputUrl = android.widget.EditText(this)
        inputUrl.setText(video.url)

        val layout = android.widget.LinearLayout(this)
        layout.orientation = android.widget.LinearLayout.VERTICAL
        layout.setPadding(50, 40, 50, 10)
        layout.addView(inputTitle)
        layout.addView(inputUrl)

        builder.setView(layout)

        builder.setPositiveButton("Save") { _, _ ->
            val newTitle = inputTitle.text.toString().trim()
            val newUrl = inputUrl.text.toString().trim()

            if (newTitle.isNotEmpty() && newUrl.isNotEmpty()) {
                val videoId = when {
                    newUrl.contains("youtube.com/watch?v=") ->
                        newUrl.substringAfter("v=").substringBefore("&")
                    newUrl.contains("youtu.be/") ->
                        newUrl.substringAfter("youtu.be/").substringBefore("?")
                    else -> ""
                }

                val newThumb = if (videoId.isNotEmpty()) {
                    "https://i.ytimg.com/vi/$videoId/hqdefault.jpg"
                } else ""

                videos[position].title = newTitle
                videos[position].url = newUrl
                videos[position].thumbnailUrl = newThumb
                adapter.notifyItemChanged(position)
            }
        }

        builder.setNegativeButton("Cancel", null)
        builder.show()
    }
}
