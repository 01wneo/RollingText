package com.yy.mobile.rollingtextview

/**
 * Created by 张宇 on 2018/2/28.
 * E-mail: zhangyu4@yy.com
 * YY: 909017428
 *
 *
 * 字符滚动变化的策略
 * strategy to determine how characters change
 */
interface CharOrderStrategy {

    /**
     * 在滚动动画计算前回调，可以做初始化的事情
     * @param sourceText 原来的文本
     * @param targetText 动画后的目标文本
     * @param charPool 外部设定的可选的字符变化序列
     */
    fun beforeCompute(sourceText: CharSequence, targetText: CharSequence, charPool: CharPool) {}

    /**
     * 从[sourceText]滚动变化到[targetText]，对于索引[index]的位置，给出变化的字符顺序
     *
     * 也可以直接继承[SimpleCharOrderStrategy]，可以更简单的实现策略
     *
     * @param sourceText 原来的文本
     * @param targetText 动画后的目标文本
     * @param index 当前字符的位置 范围[0,Math.max(sourceText.length,targetText.length)]
     * @param charPool 外部设定的可选的字符变化序列
     */
    fun findCharOrder(sourceText: CharSequence,
                      targetText: CharSequence,
                      index: Int,
                      charPool: CharPool): Pair<List<Char>, Direction>

    /**
     * 在滚动动画计算后回调
     * @param sourceText 原来的文本
     * @param targetText 动画后的目标文本
     * @param charPool 外部设定的可选的字符变化序列
     */
    fun afterCompute(sourceText: CharSequence, targetText: CharSequence, charPool: CharPool) {}
}

/**
 * 简单的策略模版
 * a simple strategy template
 */
abstract class SimpleCharOrderStrategy : CharOrderStrategy {

    override fun findCharOrder(
            sourceText: CharSequence,
            targetText: CharSequence,
            index: Int,
            charPool: CharPool): Pair<List<Char>, Direction> {

        val maxLen = Math.max(sourceText.length, targetText.length)
        val disSrc = maxLen - sourceText.length
        val disTgt = maxLen - targetText.length

        var srcChar = TextManager.EMPTY
        var tgtChar = TextManager.EMPTY
        if (index >= disSrc) {
            srcChar = sourceText[index - disSrc]
        }
        if (index >= disTgt) {
            tgtChar = targetText[index - disTgt]
        }

        return findCharOrder(srcChar, tgtChar, index, charPool)
    }

    /**
     * 从字符[sourceChar]滚动变化到[targetChar]的变化顺序
     *
     * @param sourceChar 原字符
     * @param targetChar 滚动变化后的目标字符
     * @param index 字符索引
     * @param charPool 外部设定的序列，如果没设定则为空
     */
    open fun findCharOrder(sourceChar: Char, targetChar: Char, index: Int, charPool: CharPool)
            : Pair<List<Char>, Direction> {
        val iterable = charPool.find { it.contains(sourceChar) && it.contains(targetChar) }
        return findCharOrder(sourceChar, targetChar, index, iterable)
    }

    /**
     * 从字符[sourceChar]滚动变化到[targetChar]的变化顺序
     *
     * @param sourceChar 原字符
     * @param targetChar 滚动变化后的目标字符
     * @param index 字符索引
     * @param order 外部设定的序列，如果没设定则为空
     */
    open fun findCharOrder(sourceChar: Char, targetChar: Char, index: Int, order: Iterable<Char>?)
            : Pair<List<Char>, Direction> {
        return listOf(sourceChar, targetChar) to Direction.SCROLL_DOWN
    }
}

typealias CharPool = List<Collection<Char>>

/**
 * 字符动画滚动的方向：
 *
 * [SCROLL_UP] 向上滚动
 *
 * [SCROLL_DOWN] 向下滚动
 */
enum class Direction(var value: Int) {
    SCROLL_UP(-1),
    SCROLL_DOWN(1)
}