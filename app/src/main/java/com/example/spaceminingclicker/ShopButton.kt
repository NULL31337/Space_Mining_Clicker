package com.example.spaceminingclicker

import java.math.BigInteger

class ShopButton (internal var planet: Int, internal var lvl: Int, internal var give: BigInteger, internal var costFirst: BigInteger, internal var currentLvl: Int) {
    companion object{
        fun makeNthUpgrade(n: Int, bigInteger: BigInteger): BigInteger {
            var ans = bigInteger
            repeat(n) {
                ans = (ans * 115.toBigInteger()) / 100.toBigInteger()
            }
            return ans
        }
    }
    internal var cost = makeNthUpgrade(currentLvl, costFirst)
    fun update(){
        cost = makeNthUpgrade(currentLvl, costFirst)
    }
}