package main

import java.io.File

fun main() {
    val input = File("inputs/private/day04.txt").readText().trimEnd()
    println("Part 1: ${solvePart1(input)}")
    println("Part 2: ${solvePart2(input)}")
}

data class Point(val r: Int, val c: Int) {
    fun neighbors() = listOf(
        Point(r - 1, c - 1), Point(r - 1, c), Point(r - 1, c + 1),
        Point(r, c - 1),                      Point(r, c + 1),
        Point(r + 1, c - 1), Point(r + 1, c), Point(r + 1, c + 1)
    )
}

class Grid(private val map: MutableList<CharArray>) {
    val height = map.size
    val width = map[0].size
    val indices: Sequence<Point>
        get() = sequence {
            for (r in 0 until height)
                for (c in 0 until width) yield(Point(r, c))
        }
    operator fun get(p: Point): Char = map[p.r][p.c]
    operator fun set(p: Point, char: Char) { map[p.r][p.c] = char }
    fun contains(p: Point) = p.r in 0 until height && p.c in 0 until width
    fun activeNeighbors(p: Point): Int {
        return p.neighbors().count { neighbor ->
            contains(neighbor) && this[neighbor] == '@'
        }
    }
}

private fun solvePart1(input: String): Int {
    val grid = Grid(input.lines().map { it.toCharArray() }.toMutableList())
    return grid.indices.count { point ->
        grid[point] == '@' && grid.activeNeighbors(point) < 4
    }
}

private fun solvePart2(input: String): Int {
    val grid = Grid(input.lines().map { it.toCharArray() }.toMutableList())
    return generateSequence {
        val toRemove = grid.indices
            .filter { point -> grid[point] == '@' && grid.activeNeighbors(point) < 4 }
            .toList()
        toRemove.ifEmpty { null }

    }.onEach { pointsToRemove ->
        pointsToRemove.forEach { p -> grid[p] = '.' }
    }.sumOf {
        it.size
    }
}