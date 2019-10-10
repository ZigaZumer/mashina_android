package com.example.travelorder.model

data class TravelPurpose (
    val id: Int?,
    val purpose: String
){
    constructor(name: String): this(null, name)

    override fun toString(): String {
        return purpose
    }
}