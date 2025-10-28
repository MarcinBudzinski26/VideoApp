package ie.setu.videoapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import ie.setu.videoapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val videos = mutableListOf(
            VideoModel(
                "Never Gonna Give You Up",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ",
                "https://i.ytimg.com/vi/dQw4w9WgXcQ/hqdefault.jpg"
            )
        )

        val adapter = VideoAdapter(videos) { video ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
            startActivity(intent)
        }

        binding.videoRecycler.layoutManager = LinearLayoutManager(this)
        binding.videoRecycler.adapter = adapter

        // Add new video
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
                    } else {
                        ""
                    }

                    videos.add(VideoModel(title, url, thumb))
                    adapter.notifyItemInserted(videos.size - 1)
                }
            }

            builder.setNegativeButton("Cancel", null)
            builder.show()
        }
    }
}
