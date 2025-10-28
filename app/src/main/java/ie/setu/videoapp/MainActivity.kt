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

        val videos = listOf(
            VideoModel(
                "Funny Cat Compilation",
                "https://www.youtube.com/watch?v=dQw4w9WgXcQ"
            )
        )

        val adapter = VideoAdapter(videos) { video ->
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(video.url))
            startActivity(intent)
        }

        binding.videoRecycler.layoutManager = LinearLayoutManager(this)
        binding.videoRecycler.adapter = adapter


    }
}
