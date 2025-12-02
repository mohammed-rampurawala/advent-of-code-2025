package main

import java.io.File

fun main() {
    val input = File("inputs/private/day02.txt").readText().trimEnd()
    val ranges = input.lines()[0].split(',')
    println("Part 1: ${solvePart1(ranges)}")
    println("Part 2: ${solvePart2(ranges)}")
}

private fun solvePart2(ranges: List<String>): Long {
    val ranges = ranges.map {
        val (a, b) = it.split('-')
        a.toLong()..b.toLong()
    }

    val max = ranges.maxOf { it.last }

    var sum = 0L
    val seen = mutableSetOf<Long>()

    outer@ for (unitLen in 1..18) {
        val maxRepeats = 18 / unitLen
        if (maxRepeats < 2) continue

        val start = if (unitLen == 1) 1L else pow10(unitLen - 1)
        val end = pow10(unitLen) - 1

        for (s in start..end) {
            val base = s.toString()
            for (k in 2..maxRepeats) {
                val repStr = base.repeat(k)
                val v = repStr.toLongOrNull() ?: break
                if (v > max) {
                    if (k == 2) break@outer
                    break
                }
                if (v !in seen && ranges.any { v in it }) {
                    seen += v
                    sum += v
                }
            }
        }
    }
    return sum
}

private fun solvePart1(ranges: List<String>): Long {
    val ranges = ranges.map {
        val (a, b) = it.split('-')
        a.toLong()..b.toLong()
    }

    val max = ranges.maxOf { it.last }

    var sum = 0L
    for (halfLen in 1..9) {
        val pow = pow10(halfLen)
        val start = if (halfLen == 1) 1L else pow10(halfLen - 1)
        val end = pow - 1
        for (s in start..end) {
            val v = s * pow + s
            if (v > max) break
            if (ranges.any { v in it }) sum += v
        }
    }
    return sum
}


private fun pow10(exp: Int): Long {
    var r = 1L
    repeat(exp) { r *= 10L }
    return r
}