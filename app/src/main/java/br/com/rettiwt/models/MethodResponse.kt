package br.com.rettiwt.models

import android.provider.ContactsContract

data class MethodResponse(
        val method: String?,
        val status: Int?,
        val message: String?
)

data class AuthResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: AuthData?
) {
    data class AuthData(
            val authorization: String?
    )
}

data class ConnectResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: ConnectData?
) {
    data class ConnectData(
            val socket_id: String?
    )
}

data class UpdatePostResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: UpdatePostData?
) {
    data class UpdatePostData(
            val post_id: String?,
            val count: Int?
    )
}

data class NewPostResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: PostData?
)

data class AllPostsResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: List<PostData>?
)

data class PostData(
        val id: String?,
        val rtCount: Int?,
        val starsCount: Int?,
        val image: String?,
        val createdAt: String?,
        val rtOriginalPost: PostData?,
        val text: String?,
        val postedBy: UserData?
) {

    data class UserData(
            val nickname: String?,
            val id: String?
    )

    fun toModel(): HomeItemModel {
        return if (rtOriginalPost == null) {
            HomeItemModel(id = id, stars = starsCount, rettiwts = rtCount,
                    text = text, user = postedBy?.nickname, picture = image, date = createdAt, originalPoster = null)
        } else {
            HomeItemModel(id = rtOriginalPost.id, stars = rtOriginalPost.starsCount, rettiwts = rtOriginalPost.rtCount,
                    text = rtOriginalPost.text, user = postedBy?.nickname, picture = rtOriginalPost.image,
                    date = rtOriginalPost.createdAt, originalPoster = rtOriginalPost.postedBy?.nickname)
        }
    }
}