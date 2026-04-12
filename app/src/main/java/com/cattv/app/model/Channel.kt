package com.cattv.app.model

data class Channel(
    val id: Int,
    val name: String,
    val url: String,
    val category: String,
    val logoUrl: String = "",
    val countryCode: String = "",
    val countryName: String = ""
)
