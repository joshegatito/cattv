package com.cattv.app.model

data class Country(
    val code: String,
    val name: String,
    val flag: String,
    val channels: List<Channel>
)
