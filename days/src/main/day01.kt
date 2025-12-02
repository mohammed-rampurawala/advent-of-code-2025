package main

import java.io.File
import java.lang.Math.floorMod

fun main() {
    val input = File("inputs/private/day01.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private fun solvePart1(lines: List<String>): Any {
    var position = 50
    var zeroCount = 0

    for (line in lines) {
        val dir = line[0]
        val dist = line.substring(1).toInt()
        position = when (dir) {
            'L' -> floorMod(position - dist, 100)
            'R' -> floorMod(position + dist,100)
            else -> position
        }
        if (position == 0) zeroCount++
    }

    return zeroCount
}

private fun solvePart2(lines: List<String>): Any {
    var position = 50
    var zeroCount = 0

    for (line in lines) {
        if (line.isBlank()) continue
        val dir = line[0]
        val dist = line.substring(1).toInt()

        val step = when(dir){
            'L' -> -1
            'R' -> 1
            else -> 0
        }

        for (i in 1..dist) {
            position = floorMod(position + step, 100)
            if (position == 0) zeroCount++
        }
    }

    return zeroCount
}