package com.example.a14_resell.chatlist

data class ChatListItem (

    val buyId: String,
    val sellerId: String,
    val itemTitle: String,
    val key: Long
    ){

    constructor(): this("","","",0)
}