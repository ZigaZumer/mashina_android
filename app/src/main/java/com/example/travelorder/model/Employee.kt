package com.example.travelorder.model

data class Employee (
    val id: Int?,
    val employee_name: String,
    val employee_surname: String,
    val employee_email: String,
    val employee_workplace: String
){
    constructor(name: String): this(null, name, "", "", "")

    override fun toString(): String {
        return "$employee_name $employee_surname"
    }
}