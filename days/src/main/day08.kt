package main

import java.io.File

fun main() {
    // Replace public with private when cloning the repo for personal use
    // Ensure this path matches where you save your input data
    val input = File("inputs/private/day08.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private fun solvePart1(lines: List<String>): Long {
    // 1. Parse Input into Points
    val points = lines.filter { it.isNotBlank() }.mapIndexed { index, line ->
        val parts = line.split(",").map { it.trim().toInt() }
        Point(index, parts[0], parts[1], parts[2])
    }

    // 2. Generate all possible pairs (Edges) and calculate squared distance
    val edges = ArrayList<Edge>()
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val p1 = points[i]
            val p2 = points[j]
            val distSq = p1.distSq(p2)
            edges.add(Edge(i, j, distSq))
        }
    }

    // 3. Sort edges by distance (shortest first)
    // We use squared distance to avoid expensive sqrt calls and float precision issues
    edges.sortBy { it.distSq }

    // 4. Connect the 1000 pairs of junction boxes which are closest together
    val dsu = Dsu(points.size)
    // The problem asks us to process the top 1000 pairs from the sorted list.
    // We use minOf to handle cases where the input might generate fewer than 1000 edges.
    val limit = minOf(1000, edges.size)

    for (i in 0 until limit) {
        val edge = edges[i]
        // The DSU union logic handles the rule:
        // "Because these two... were already in the same circuit, nothing happens!"
        dsu.union(edge.u, edge.v)
    }

    // 5. Get circuit sizes, sort descending, and multiply the top 3
    val sizes = dsu.getComponentSizes().sortedDescending()

    // Safety check in case there are fewer than 3 circuits (unlikely with puzzle input)
    if (sizes.size < 3) return 0L

    return sizes[0].toLong() * sizes[1] * sizes[2]
}

private fun solvePart2(lines: List<String>): Long {
    val points = parsePoints(lines)
    val edges = generateSortedEdges(points)

    val dsu = Dsu3D(points.size)
    var activeComponents = points.size

    // Iterate through ALL edges from shortest to longest
    for (edge in edges) {
        val rootU = dsu.find(edge.u)
        val rootV = dsu.find(edge.v)

        if (rootU != rootV) {
            dsu.union(edge.u, edge.v)
            activeComponents--

            // Check if this connection unified the entire graph
            if (activeComponents == 1) {
                val p1 = points[edge.u]
                val p2 = points[edge.v]
                return p1.x.toLong() * p2.x.toLong()
            }
        }
    }

    return 0L
}

// --- Shared Helper Functions ---

private fun parsePoints(lines: List<String>): List<Point3D> {
    return lines.filter { it.isNotBlank() }.mapIndexed { index, line ->
        val parts = line.split(",").map { it.trim().toInt() }
        Point3D(index, parts[0], parts[1], parts[2])
    }
}

private fun generateSortedEdges(points: List<Point3D>): List<Edge3D> {
    val edges = ArrayList<Edge3D>()
    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val p1 = points[i]
            val p2 = points[j]
            edges.add(Edge3D(i, j, p1.distSq(p2)))
        }
    }
    edges.sortBy { it.distSq }
    return edges
}

// --- Data Structures ---

private data class Point3D(val id: Int, val x: Int, val y: Int, val z: Int) {
    fun distSq(other: Point3D): Long {
        val dx = (x - other.x).toLong()
        val dy = (y - other.y).toLong()
        val dz = (z - other.z).toLong()
        return dx * dx + dy * dy + dz * dz
    }
}

private data class Edge3D(val u: Int, val v: Int, val distSq: Long)

private class Dsu3D(size: Int) {
    private val parent = IntArray(size) { it }
    private val size = IntArray(size) { 1 }

    fun find(i: Int): Int {
        if (parent[i] != i) {
            parent[i] = find(parent[i]) // Path compression
        }
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val rootI = find(i)
        val rootJ = find(j)

        if (rootI != rootJ) {
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ
                size[rootJ] += size[rootI]
            } else {
                parent[rootJ] = rootI
                size[rootI] += size[rootJ]
            }
        }
    }

    fun getComponentSizes(): List<Int> {
        val componentSizes = mutableMapOf<Int, Int>()
        for (i in parent.indices) {
            val root = find(i)
            componentSizes[root] = size[root]
        }
        return componentSizes.values.toList()
    }
}

// --- Helper Data Structures ---

private data class Point(val id: Int, val x: Int, val y: Int, val z: Int) {
    fun distSq(other: Point): Long {
        val dx = (x - other.x).toLong()
        val dy = (y - other.y).toLong()
        val dz = (z - other.z).toLong()
        return dx * dx + dy * dy + dz * dz
    }
}

data class Edge(val u: Int, val v: Int, val distSq: Long)

class Dsu(size: Int) {
    private val parent = IntArray(size) { it }
    private val size = IntArray(size) { 1 }

    fun find(i: Int): Int {
        if (parent[i] != i) {
            parent[i] = find(parent[i]) // Path compression
        }
        return parent[i]
    }

    fun union(i: Int, j: Int) {
        val rootI = find(i)
        val rootJ = find(j)

        if (rootI != rootJ) {
            // Union by size
            if (size[rootI] < size[rootJ]) {
                parent[rootI] = rootJ
                size[rootJ] += size[rootI]
            } else {
                parent[rootJ] = rootI
                size[rootI] += size[rootJ]
            }
        }
    }

    fun getComponentSizes(): List<Int> {
        val componentSizes = mutableMapOf<Int, Int>()
        for (i in parent.indices) {
            val root = find(i)
            componentSizes[root] = size[root]
        }
        return componentSizes.values.toList()
    }
}