package br.com.rettiwt

const val REQUEST_IMAGE = 101

const val METHOD_AUTH = "auth"
const val METHOD_SEND = "send"
const val METHOD_KEEP_ALIVE = "keepAlive"
const val METHOD_RETTIWT = "rettiwt"
const val METHOD_STAR = "star"
const val METHOD_GET_ALL = "getAllPosts"
const val METHOD_NEW_POST = "newPost"
const val METHOD_NEW_RETTIWT = "newRt"
const val METHOD_NEW_STAR = "newStar"
const val METHOD_CONNECT = "connect"

val allMethods = listOf(METHOD_AUTH, METHOD_CONNECT, METHOD_GET_ALL, METHOD_KEEP_ALIVE, METHOD_NEW_POST, METHOD_NEW_RETTIWT, METHOD_NEW_STAR, METHOD_RETTIWT, METHOD_SEND, METHOD_STAR)

const val APP_ID = "dad09ff8f0fdf3f11e1e26dd72fa395dea59b414"