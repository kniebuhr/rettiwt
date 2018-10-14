package br.com.rettiwt.models

data class MethodRequest(
        val method: String?,
        val appId: String?,
        val authorization: String?,
        val params: Any?
)

data class AuthParams(
        val nickname: String?,
        val password: String?
)

data class SendParams(
        val text: String?,
        val image: String? = null
)

data class KeepAliveParams(
        val socket_id: String?
)

data class StarParams(
        val post_id: String?
)

data class RettiwtParams(
        val post_id: String?
)