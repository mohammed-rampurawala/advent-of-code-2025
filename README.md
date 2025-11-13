# Advent of Code 2025

Repository for solving Advent of Code 2025 puzzles.

Project layout
- `templates/` — language templates for each day
- `days/` — per-day solution folders (day01 .. day25)
- `inputs/` — puzzle input files (one file per day)
- `tests/` — test data and unit tests
- `utils/` — shared helpers

Quick start
1. Copy a template from `templates/` into the appropriate `days/dayNN/` folder.
2. Place the puzzle input in `inputs/dayNN.txt`.
3. Run the solution using your language/runtime or test runner.

Templates

Kotlin template (save as `templates/kotlin/template.kt`):
```kotlin
fun main() {
    val input = java.io.File("inputs/day01.txt").readText().trimEnd()
    val lines = input.lines()
    // part 1
    println("Part 1: ${solvePart1(lines)}")
    // part 2
    println("Part 2: ${solvePart2(lines)}")
}

fun solvePart1(lines: List<String>): Any {
    // implement
    return 0
}

fun solvePart2(lines: List<String>): Any {
    // implement
    return 0
}
