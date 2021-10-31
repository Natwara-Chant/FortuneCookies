package com.egci428.a11189

object mainWishes {
    private val defaultWishes = ArrayList<Wish>()
    fun getWish(): ArrayList<Wish>{
        return defaultWishes
    }

    init {
        defaultWishes.add(Wish("You're Lucky", "positive"))
        defaultWishes.add(Wish("You will get A", "positive"))
        defaultWishes.add(Wish("Don't Panic", "negative"))
    }
}