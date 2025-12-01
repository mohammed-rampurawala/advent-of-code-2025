package main

import java.io.File

fun main() {
    val input = File("inputs/private/day01.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

fun solvePart1(lines: List<String>): Any {
    var position = 50
    var zeroCount = 0

    for (line in lines) {
        val dir = line[0]
        val dist = line.substring(1).toInt()
        position = when (dir) {
            'L' -> ((position - dist) % 100 + 100) % 100
            'R' -> (position + dist) % 100
            else -> position
        }
        if (position == 0) zeroCount++
    }

    return zeroCount
}

fun solvePart2(lines: List<String>): Any {
    var position = 50
    var zeroCount = 0

    for (line in lines) {
        if (line.isBlank()) continue
        val dir = line[0]
        val dist = line.substring(1).toInt()

        val occurrences = when (dir) {
            'R' -> {
                var firstK = (100 - (position % 100)) % 100
                if (firstK == 0) firstK = 100
                if (firstK > dist) 0 else 1 + (dist - firstK) / 100
            }
            'L' -> {
                var firstK = position % 100
                if (firstK == 0) firstK = 100
                if (firstK > dist) 0 else 1 + (dist - firstK) / 100
            }
            else -> 0
        }

        zeroCount += occurrences

        position = when (dir) {
            'L' -> ((position - dist) % 100 + 100) % 100
            'R' -> (position + dist) % 100
            else -> position
        }
    }

    return zeroCount
}