package main

import java.io.File

fun main() {
    val input = File("inputs/private/day06.txt").readText().trimEnd()
    val lines = input.lines()
    println("Part 1: ${solvePart1(lines)}")
    println("Part 2: ${solvePart2(lines)}")
}

private fun solvePart1(lines: List<String>): Long {
    if (lines.isEmpty()) return 0L

    val numberLines = lines.dropLast(1)
    val operatorLine = lines.last()

    val columnsMap = mutableMapOf<Int, MutableList<Long>>()

    numberLines.forEach { line ->
        val tokens = line.trim().split(" ") // Split by whitespace
        tokens.forEachIndexed { index, token ->
            if (token.isNotEmpty()) {
                columnsMap.computeIfAbsent(index) { mutableListOf() }.add(token.toLong())
            }
        }
    }

    val operators = operatorLine.trim().split("\\s+".toRegex())
    var totalSum = 0L

    operators.forEachIndexed { index, opSymbol ->
        val operator = Operator.fromSymbol(opSymbol)
        val numbers = columnsMap[index]

        if (operator != null && numbers != null && numbers.isNotEmpty()) {
            val columnResult = when (operator) {
                Operator.ADD -> numbers.sum()
                else -> numbers.reduce { acc, next -> operator.apply(acc, next) }
            }
            totalSum += columnResult
        }
    }

    return totalSum
}

private fun solvePart2(lines: List<String>): Long {
    if (lines.isEmpty()) return 0L

    val maxLength = lines.maxOf { it.length }
    val grid = lines.map { it.padEnd(maxLength, ' ') }

    val blocks = mutableListOf<Block>()
    val currentBuffer = mutableListOf<String>()

    for (x in 0 until maxLength) {
        val columnStr = grid.extractColumnString(x)

        if (columnStr.isBlank()) {
            if (currentBuffer.isNotEmpty()) {
                blocks.add(Block(currentBuffer.toList()))
                currentBuffer.clear()
            }
        } else {
            currentBuffer.add(columnStr)
        }
    }

    if (currentBuffer.isNotEmpty()) {
        blocks.add(Block(currentBuffer.toList()))
    }

    return blocks.sumOf { it.solve() }
}

enum class Operator(val symbol: String) {
    ADD("+") {
        override fun apply(a: Long, b: Long) = a + b
    },
    MULTIPLY("*") {
        override fun apply(a: Long, b: Long) = a * b
    },
    DIVIDE("/") {
        override fun apply(a: Long, b: Long) = a / b
    },
    SUBTRACT("-") {
        override fun apply(a: Long, b: Long) = a - b
    };

    abstract fun apply(a: Long, b: Long): Long

    companion object {
        fun fromSymbol(symbol: String): Operator? = entries.find { it.symbol == symbol }
        fun findInColumn(chars: CharSequence): Operator? = entries.find { op -> chars.contains(op.symbol) }
    }
}

/**
 * Represents a discrete visual block from the Cephalopod worksheet.
 * @param columns A list of strings, where each string represents a vertical column from the grid.
 */
data class Block(val columns: List<String>) {

    fun solve(): Long {
        // 1. Find Operator (usually in the bottom row / somewhere in the block)
        // We look through all columns to find a symbol.
        val operator = columns.firstNotNullOfOrNull { col ->
            Operator.findInColumn(col)
        } ?: Operator.ADD // Default to ADD if none found (fallback)

        // 2. Parse numbers
        // Logic: "Reading the problems right-to-left one column at a time"
        val numbers = columns.reversed().mapNotNull { col ->
            parseColumnToNumber(col)
        }

        if (numbers.isEmpty()) return 0L

        // 3. Calculate
        // Use the first number as the accumulator start
        var result = numbers.first()
        for (i in 1 until numbers.size) {
            result = operator.apply(result, numbers[i])
        }

        return result
    }

    private fun parseColumnToNumber(columnStr: String): Long? {
        // Top digit is most significant. Filter out non-digits (spaces/operators).
        val digitString = columnStr.filter { it.isDigit() }
        return if (digitString.isNotEmpty()) digitString.toLong() else null
    }
}

// Extension to extract a vertical slice of the grid as a String
private fun List<String>.extractColumnString(x: Int): String {
    val builder = StringBuilder(this.size)
    for (line in this) {
        // Safe access in case line is shorter (though we padded earlier)
        if (x < line.length) builder.append(line[x]) else builder.append(' ')
    }
    return builder.toString()
}