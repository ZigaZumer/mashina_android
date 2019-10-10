package com.example.travelorder.utils

import org.joda.time.LocalDateTime
import org.joda.time.format.DateTimeFormat

class DateUtil {

    val BASE_DATE_TIME_FORMAT_FROM_DB = "yyy-MM-dd'T'HH:mm:ss.SSS'Z'"


    fun getLocalDateObjectFromString(stringDate: String): LocalDateTime {
//        val format = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val format = DateTimeFormat.forPattern(BASE_DATE_TIME_FORMAT_FROM_DB)
        return format.parseLocalDateTime(stringDate)
    }

    fun getStringDateFromLocalDateTimeObject(localDateTime: LocalDateTime): String{
//        val formatter = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")
        val formatter = DateTimeFormat.forPattern(BASE_DATE_TIME_FORMAT_FROM_DB)
        return formatter.print(localDateTime)
    }

    fun getPresentationStringDate(stringDate: String): String{
        val dateObject = getLocalDateObjectFromString(stringDate)
        val format = DateTimeFormat.forPattern("dd.MM.yyyy / HH:mm")
        return format.print(dateObject)
    }

    fun getPresentationDateFromObject(localDateTime: LocalDateTime): String{
        val formatter = DateTimeFormat.forPattern("dd.MM.yyyy")
        return formatter.print(localDateTime)
    }

    fun getPresentationDateFromString(stringDate: String): String{
        val dateObject = getLocalDateObjectFromString(stringDate)
        val format = DateTimeFormat.forPattern("dd.MM.yyyy")
        return format.print(dateObject)
    }
}