package com.example.travelorder.model


data class Order (
    val id: Int,
    val employee_name: String,
    val employee_surname: String,
    val issue_date: String,
    val purpose: String,
    val from_location: String,
    val to_location: String,
    val estimated_departure: String,
    val estimated_arrival: String,
    val estimated_duration: String,
    val vehicle_name: String,
    val vehicle_registration: String,
    val mileage_end: Int?,
    val mileage_start: Int,
    val description: String
)