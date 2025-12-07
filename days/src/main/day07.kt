package main

import java.io.File

fun main() {
    val input = File("inputs/private/day07.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}
// Time complexity: O(H*W)
fun solvePart1(lines: List<String>): Long {
    val startX = lines.first().indexOf('S')
    val visited = mutableSetOf<String>()
    return traceBeam(lines, startX, 0, visited)
}

fun traceBeam(grid: List<String>, x: Int, y: Int, visited: MutableSet<String>): Long {
    if (y >= grid.size || x !in 0 until grid[0].length) {
        return 0
    }

    val positionKey = "$x,$y"

    if (visited.contains(positionKey)) {
        return 0
    }

    visited.add(positionKey)


    val char = grid[y][x]

    return if (char == '^') {
        1 + traceBeam(grid, x - 1, y + 1, visited) + traceBeam(grid, x + 1, y + 1, visited)
    } else {
        traceBeam(grid, x, y + 1, visited)
    }
}

// Time complexity: O(2^k) where K is the number of splitters on a path
fun solvePart2(lines: List<String>): Long {
    val startX = lines.first().indexOf('S')
    return traceTimeline(lines, startX, 0)
}

fun traceTimeline(grid: List<String>, x: Int, y: Int): Long {
    if (y >= grid.size) {
        return 1L
    }
    if (x !in 0 until grid[0].length) {
        return 1L
    }

    val currentTile = grid[y][x]

    return if (currentTile == '^') {
        val leftTimelineCount = traceTimeline(grid, x - 1, y + 1)
        val rightTimelineCount = traceTimeline(grid, x + 1, y + 1)
        leftTimelineCount + rightTimelineCount
    } else {
        traceTimeline(grid, x, y + 1)
    }
}