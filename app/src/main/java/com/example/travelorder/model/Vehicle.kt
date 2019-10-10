package com.example.travelorder.model

data class Vehicle (
    val id: Int?,
    val vehicle_name: String,
    val vehicle_registration: String,
    val vehicle_beginning_mileage: Int?
){
    constructor(name: String): this(null, name, "", null)

    override fun toString(): String {
        return "$vehicle_name $vehicle_registration"
    }
}