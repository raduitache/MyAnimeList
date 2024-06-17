package com.raduitache.myanimelist.forum.models

import com.google.gson.annotations.SerializedName

data class Data(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("number_of_posts") var numberOfPosts: Int? = null,
    @SerializedName("last_post_created_at") var lastPostCreatedAt: String? = null,
    @SerializedName("is_locked") var isLocked: Boolean? = null,
    @SerializedName("created_by") var createdBy: CreatedBy? = CreatedBy(),
    @SerializedName("last_post_created_by") var lastPostCreatedBy: LastPostCreatedBy? = LastPostCreatedBy()
)

data class LastPostCreatedBy(

    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null

)

data class CreatedBy(
    @SerializedName("id") var id: Int? = null,
    @SerializedName("name") var name: String? = null
)
