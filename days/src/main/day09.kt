package main

import java.io.File
import kotlin.math.max
import kotlin.math.min

private data class Day9Point(val x: Long, val y: Long)
private data class Day9Edge(val p1: Day9Point, val p2: Day9Point) {
    val isVertical = p1.x == p2.x
    val minX = min(p1.x, p2.x)
    val maxX = max(p1.x, p2.x)
    val minY = min(p1.y, p2.y)
    val maxY = max(p1.y, p2.y)
}


fun main(){
    val input = File("inputs/private/day09.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private data class Rect(val x1: Long, val y1: Long, val x2: Long, val y2: Long) {
    val minX = min(x1, x2)
    val maxX = max(x1, x2)
    val minY = min(y1, y2)
    val maxY = max(y1, y2)
    val width = maxX - minX + 1
    val height = maxY - minY + 1
    val area = width * height
}

private fun solvePart1(lines: List<String>): Long {
    val Day9Points = parseDay9Points(lines)
    var maxArea = 0L

    for (i in Day9Points.indices) {
        for (j in i + 1 until Day9Points.size) {
            val rect = Rect(Day9Points[i].x, Day9Points[i].y, Day9Points[j].x, Day9Points[j].y)
            maxArea = max(maxArea, rect.area)
        }
    }
    return maxArea
}

private fun solvePart2(lines: List<String>): Long {
    val points = parseDay9Points(lines)
    var maxArea = 0L

    val day9Edges = points.zip(points.drop(1) + points.take(1)) { p1, p2 ->
        Day9Edge(p1, p2)
    }

    for (i in points.indices) {
        for (j in i + 1 until points.size) {
            val rect = Rect(points[i].x, points[i].y, points[j].x, points[j].y)

            // Optimization: Don't check expensive geometry if area isn't bigger
            if (rect.area <= maxArea) continue

            if (isRectInsidePolygon(rect, day9Edges)) {
                maxArea = rect.area
            }
        }
    }
    return maxArea
}

// --- Helper Classes & Functions ---

private fun parseDay9Points(lines: List<String>): List<Day9Point> {
    return lines
        .filter { it.isNotBlank() }
        .map { line ->
            val (x, y) = line.split(",").map { it.trim().toLong() }
            Day9Point(x, y)
        }
}


/**
 * Checks if a rectangle is entirely within the polygon defined by [day9Edges].
 * Criteria:
 * 1. The center of the rectangle must be inside the polygon.
 * 2. No polygon edge may strictly intersect the rectangle's interior.
 */
private fun isRectInsidePolygon(rect: Rect, day9Edges: List<Day9Edge>): Boolean {
    // 1. Ray Casting Check (Is Center Inside?)
    // We use floating Day9Point mid-coordinates to represent the "center" of the tiles
    val midX = (rect.minX + rect.maxX) / 2.0
    val midY = (rect.minY + rect.maxY) / 2.0

    var intersections = 0
    for (edge in day9Edges) {
        // Only interested in vertical edges for horizontal ray casting
        if (edge.isVertical) {
            // Check if ray at Y=midY crosses this edge
            // Standard inclusion: y_min <= y < y_max (avoids double counting vertices)
            if (edge.minY <= midY && edge.maxY > midY) {
                if (edge.p1.x > midX) {
                    intersections++
                }
            }
        }
    }

    // If intersections is even, we are outside
    if (intersections % 2 == 0) return false

    // 2. Intersection Check (Does any edge slice through the rectangle?)
    for (edge in day9Edges) {
        if (edge.isVertical) {
            // A vertical edge cuts through if its X is strictly inside the Rect X-range
            // AND its Y-range overlaps the Rect Y-range strictly
            if (edge.p1.x > rect.minX && edge.p1.x < rect.maxX) {
                // Check Y overlap
                val overlapStart = max(edge.minY, rect.minY)
                val overlapEnd = min(edge.maxY, rect.maxY)
                if (overlapStart < overlapEnd) return false
            }
        } else {
            // Horizontal edge check
            if (edge.p1.y > rect.minY && edge.p1.y < rect.maxY) {
                // Check X overlap
                val overlapStart = max(edge.minX, rect.minX)
                val overlapEnd = min(edge.maxX, rect.maxX)
                if (overlapStart < overlapEnd) return false
            }
        }
    }

    return true
}

