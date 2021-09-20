package com.example.videos.network

import  com.example.videos.database.DatabaseVideo
import com.example.videos.domain.Video
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkVideoContainer(val videos: List<NetworkVideo>)

@JsonClass(generateAdapter = true)
data class NetworkVideo(
        val title: String,
        val description: String,
        val url: String,
        val updated: String,
        val thumbnail: String,
        val closedCaptions: String?)

fun NetworkVideoContainer.asDomainModel(): List<Video> {
    return videos.map {
        Video(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }
}

fun NetworkVideoContainer.asDatabaseModel(): Array<DatabaseVideo> {
    return videos.map {
        DatabaseVideo(
                title = it.title,
                description = it.description,
                url = it.url,
                updated = it.updated,
                thumbnail = it.thumbnail)
    }.toTypedArray()
}
