package com.example.a14_resell.chatdetail

data class ChatItem(
    val senderId: String,
    val message: String
){
    constructor(): this("","")
}
