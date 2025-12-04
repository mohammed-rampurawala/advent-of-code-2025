package template

import java.io.File

fun main() {
    // Replace public with private when cloning the repo for personal use
    val input = File("inputs/public/dayNN.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private fun solvePart1(lines: List<String>): Long {
    return 0
}

private fun solvePart2(lines: List<String>): Long {
    return 0
}