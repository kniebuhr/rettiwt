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

data class PostResponse(
        val method: String?,
        val status: Int?,
        val message: String?,
        val data: PostData?
) {
    data class PostData(
            val id: String?,
            var rtCount: Int?,
            var starsCount: Int?,
            val text: String?,
            val postedBy: UserData?
    )

    data class UserData(
            val nickname: String?,
            val id: String?
    )

    fun toModel(): HomeItemModel {
        return HomeItemModel(id = data?.id, stars = data?.starsCount, rettiwts = data?.rtCount,
                text = data?.text, user = data?.postedBy?.nickname, picture = null, video = null, date = null)
    }
}