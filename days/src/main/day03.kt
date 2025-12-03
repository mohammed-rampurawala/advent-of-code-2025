package main

import java.io.File

fun main() {
    val input = File("inputs/private/day03.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private fun solvePart1(lines: List<String>): Long {
    var sum: Long = 0
    lines.forEach { line ->
        var highest = 0
        var right = Int.MIN_VALUE
        for (i in line.length - 1 downTo 0) {
            var digit = line[i].digitToInt()
            if (right != Int.MIN_VALUE) {
                val curr = digit * 10 + right
                if (curr > highest) {
                    highest = curr
                }
            }
            if (digit > right) {
                right = digit
            }
        }

        sum += highest

    }
    return sum
}

private fun solvePart2(lines: List<String>): Long {
    var sum = 0L
    lines.forEach { line ->
        val n = line.length
        var start = 0
        var value = 0L
        for (pos in 0 until 12) {
            val upper = n - (12 - pos)
            var bestDigit = -1
            var bestIdx = start
            for (i in start..upper) {
                val d = line[i].digitToInt()
                if (d > bestDigit) {
                    bestDigit = d
                    bestIdx = i
                    if (bestDigit == 9) break
                }
            }
            value = value * 10 + bestDigit
            start = bestIdx + 1
        }
        sum += value
    }
    return sum
}