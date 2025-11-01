package ie.setu.videoapp

import org.junit.Assert.*
import org.junit.Test

class UnitTesting {

    @Test
    fun testCreateVideo() {
        val video = VideoModel(
            title = "Test Video",
            url = "https://youtu.be/test123",
            thumbnailUrl = "https://i.ytimg.com/vi/test123/hqdefault.jpg"
        )

        assertEquals("Test Video", video.title)
        assertEquals("https://youtu.be/test123", video.url)
        assertEquals("https://i.ytimg.com/vi/test123/hqdefault.jpg", video.thumbnailUrl)
    }

    @Test
    fun testReadVideosList() {
        val videos = mutableListOf(
            VideoModel("A", "url1", "thumb1"),
            VideoModel("B", "url2", "thumb2")
        )

        assertEquals(2, videos.size)
        assertEquals("A", videos[0].title)
    }

    @Test
    fun testUpdateVideoTitle() {
        val video = VideoModel("Old Title", "url", "thumb")
        video.title = "New Title"

        assertEquals("New Title", video.title)
    }

    @Test
    fun testDeleteVideo() {
        val videos = mutableListOf(
            VideoModel("A", "url1", "thumb1"),
            VideoModel("B", "url2", "thumb2")
        )

        videos.removeAt(0)
        assertEquals(1, videos.size)
        assertEquals("B", videos[0].title)
    }
}
