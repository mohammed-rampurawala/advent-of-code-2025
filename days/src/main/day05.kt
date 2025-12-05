package template

import java.io.File
import kotlin.math.max

fun main() {
    val input = File("inputs/private/day05.txt").readText().trimEnd()
    val pair = prepareData(input.lines())
    println("Part 1: ${solvePart1(pair.first, pair.second)}")
    println("Part 2: ${solvePart2(pair.first)}")
}

private fun prepareData(lines: List<String>): Pair<List<Range>, List<Long>> {
    val ranges = mutableListOf<Range>()
    val numbers = mutableListOf<Long>()
    var isBlankLineFound = false
    for (line in lines) {
        if (line == "") {
            isBlankLineFound = true
            continue
        }
        if (isBlankLineFound.not()) {
            val parts = line.split("-")
            val start = parts[0].toLong()
            val end = parts[1].toLong()
            ranges.add(Range(start, end))
        } else {
            numbers.add(line.toLong())
        }

    }
    return Pair(ranges, numbers)
}

private fun solvePart1(ranges: List<Range>, numbers:List<Long>): Long {
    var count: Long = 0
    for(number in numbers){
        for(range in ranges){
            if(number in range.start..range.end){
                count++
                break
            }
        }
    }
    return count
}

private fun solvePart2(ranges: List<Range>): Long {
    val sortedRanges = ranges.sortedBy { it.start }

    val mergedRanges = mutableListOf<Range>()
    var current = sortedRanges[0]

    for (i in 1 until sortedRanges.size) {
        val next = sortedRanges[i]
        if (current.overlaps(next)) {
            val newEnd = max(current.end, next.end)
            current = current.copy(end = newEnd)
        } else {
            mergedRanges.add(current)
            current = next
        }
    }

    mergedRanges.add(current)
    return mergedRanges.sumOf { it.size() }
}

data class Range(val start: Long, val end: Long) {
    fun contains(other: Range): Boolean {
        return this.start <= other.start && this.end >= other.end
    }
    fun overlaps(other: Range): Boolean {
        return this.end >= other.start
    }
    fun size(): Long = end - start + 1
}