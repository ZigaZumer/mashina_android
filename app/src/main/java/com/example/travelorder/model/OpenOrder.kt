package com.example.travelorder.model

data class OpenOrder (
    val issue_date: String,
    val driver_id: Int,
    val purpose_id: Int,
    val from_location: String,
    val to_location: String,
    val estimated_departure: String,
    val estimated_arrival: String,
    val vehicle_id: Int,
    val description: String,
    val mileage_end: Int?
)