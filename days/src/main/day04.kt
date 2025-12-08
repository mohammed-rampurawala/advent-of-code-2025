package main

import java.io.File

fun main() {
    val input = File("inputs/private/day04.txt").readText().trimEnd()
    println("Part 1: ${solvePart1(input)}")
    println("Part 2: ${solvePart2(input)}")
}

data class DataPoint(val r: Int, val c: Int) {
    fun neighbors() = listOf(
        DataPoint(r - 1, c - 1), DataPoint(r - 1, c), DataPoint(r - 1, c + 1),
        DataPoint(r, c - 1),                      DataPoint(r, c + 1),
        DataPoint(r + 1, c - 1), DataPoint(r + 1, c), DataPoint(r + 1, c + 1)
    )
}

class Grid(private val map: MutableList<CharArray>) {
    val height = map.size
    val width = map[0].size
    val indices: Sequence<DataPoint>
        get() = sequence {
            for (r in 0 until height)
                for (c in 0 until width) yield(DataPoint(r, c))
        }
    operator fun get(p: DataPoint): Char = map[p.r][p.c]
    operator fun set(p: DataPoint, char: Char) { map[p.r][p.c] = char }
    fun contains(p: DataPoint) = p.r in 0 until height && p.c in 0 until width
    fun activeNeighbors(p: DataPoint): Int {
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