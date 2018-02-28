package com.yy.mobile.rollingtextview

import java.util.*

/**
 * Created by 张宇 on 2018/2/28.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 */
internal class CharOrderManager {

    var charStrategy: CharOrderStrategy = QuickAnimation

    private val charOrderList = mutableListOf<LinkedHashSet<Char>>()

    fun addCharOrder(orderList: Iterable<Char>) {
        val list = mutableListOf(TextManager.EMPTY)
        list.addAll(orderList)
        val set = LinkedHashSet<Char>(list)
        charOrderList.add(set)
    }

    fun findCharOrder(sourceText: CharSequence, targetText: CharSequence, index: Int): Pair<List<Char>, Direction> {
        return charStrategy.findCharOrder(sourceText, targetText, index, charOrderList)
    }
}