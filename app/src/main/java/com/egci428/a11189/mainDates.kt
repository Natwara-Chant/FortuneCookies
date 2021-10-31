package com.egci428.a11189

object mainDates {
    private val dateData = ArrayList<Date>()
    fun getDate(): ArrayList<Date>{
        return dateData
    }

    init {
        dateData.add(Date("Date: 30-Oct-2021 09:00"))
        dateData.add(Date("Date: 30-Oct-2021 09:00"))
        dateData.add(Date("Date: 30-Oct-2021 09:00"))
    }
}