package main

import java.io.File

fun main() {
    val input = File("inputs/private/day07.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    // It is taking too long time so I used an optimized version below
//    println("Part 2: ${solvePart2(lines)}")
    val manifold = Manifold.parse(input)
    println("Part 2: ${solvePart2Optimized(manifold)}")
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

fun solvePart2Optimized(manifold: Manifold): Long {
    // In Part 2, we track the COUNT of timelines at each position.
    // If two timelines converge, they do not merge physically;
    // they just sum up (Example: 1 path from left + 1 path from right = 2 timelines here).

    // Map: Column Index -> Count of Active Timelines
    var activeTimelines = mapOf(manifold.startCol to 1L)

    // Track timelines that exit the manifold boundaries sideways
    var finishedTimelines = 0L

    for (y in 0 until manifold.height) {
        val nextTimelines = mutableMapOf<Int, Long>()

        for ((x, count) in activeTimelines) {
            // Handle beams that drifted out of bounds in the previous step
            if (!manifold.contains(x)) {
                finishedTimelines += count
                continue
            }

            when (manifold.getComponent(x, y)) {
                Component.SPLITTER -> {
                    // Quantum Split: Time splits. The count duplicates to both sides.
                    // Left Path
                    nextTimelines.merge(x - 1, count, Long::plus)
                    // Right Path
                    nextTimelines.merge(x + 1, count, Long::plus)
                }
                else -> {
                    // Pass through: The count moves straight down
                    nextTimelines.merge(x, count, Long::plus)
                }
            }
        }

        activeTimelines = nextTimelines
        if (activeTimelines.isEmpty()) break
    }

    // Total = Timelines that exited sideways + Timelines that reached the bottom
    return finishedTimelines + activeTimelines.values.sum()
}

// ==========================================
// Domain Models
// ==========================================

enum class Component(val char: Char) {
    START('S'),
    SPLITTER('^'),
    EMPTY('.');

    companion object {
        fun fromChar(c: Char): Component = entries.find { it.char == c } ?: EMPTY
    }
}

/**
 * Represents the fixed structure of the Tachyon Manifold.
 */
data class Manifold(
    private val grid: List<String>,
    val startCol: Int
) {
    val height: Int = grid.size
    val width: Int = grid.firstOrNull()?.length ?: 0

    fun getComponent(x: Int, y: Int): Component {
        if (y !in grid.indices || x !in 0 until width) return Component.EMPTY
        return Component.fromChar(grid[y][x])
    }

    fun contains(x: Int): Boolean = x in 0 until width

    companion object {
        fun parse(input: String): Manifold {
            val lines = input.lines()
            // Find 'S' in the first row
            val startCol = lines.firstOrNull()?.indexOf('S') ?: -1
            return Manifold(lines, startCol)
        }
    }
}