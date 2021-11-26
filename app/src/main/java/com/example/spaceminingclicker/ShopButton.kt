package com.example.spaceminingclicker

import java.math.BigInteger

enum class typeOfButton {
    AFK,
    NIGHT,
    CLICK,
}

class ShopButton(
    internal var planet: Int,
    internal var lvl: Int,
    internal var give: BigInteger,
    private var costFirst: BigInteger,
    internal var currentLvl: Int,
    internal var type: typeOfButton
) {
    companion object {
        fun makeNthUpgrade(n: Int, bigInteger: BigInteger, type: typeOfButton): BigInteger {
            var ans = bigInteger
            when (type) {
                typeOfButton.AFK -> repeat(n) {
                    ans = (ans * 115.toBigInteger()) / 100.toBigInteger()
                }
                typeOfButton.CLICK -> repeat(n) {
                    ans = (ans * 250.toBigInteger()) / 100.toBigInteger()
                }
            }
            return ans
        }
    }

    internal var cost = makeNthUpgrade(currentLvl, costFirst, type)
    fun update() {
        cost = makeNthUpgrade(currentLvl, costFirst, type)
    }
}