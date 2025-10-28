package ie.setu.videoapp

data class VideoModel(
    var title: String,
    var url: String,
    var thumbnailUrl: String = ""
)
