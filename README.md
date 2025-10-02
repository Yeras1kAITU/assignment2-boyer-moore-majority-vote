# Boyer-Moore Majority Vote Algorithm

## Overview
This project implements the Boyer-Moore Majority Vote algorithm for finding the majority element in a sequence (element appearing more than n/2 times) with O(n) time complexity and O(1) space complexity.

## Algorithm Description
The algorithm works in two phases:
1. **Candidate Selection**: Traverse the array maintaining a candidate and count
2. **Candidate Verification**: Verify if the candidate appears more than n/2 times

## Complexity Analysis
- **Time Complexity**: Θ(n), O(n), Ω(n) - Always makes exactly 2 passes
- **Space Complexity**: Θ(1), O(1), Ω(1) - Uses only constant extra space
- **Comparisons**: 2n comparisons in worst case
- **Memory**: O(1) auxiliary space

## Features
- Clean, documented Java implementation (Java 25)
- Comprehensive unit tests with JUnit 5
- Performance metrics collection (comparisons, array accesses, timing)
- CLI interface for benchmarking with configurable input sizes
- Support for multiple data types (generics)
- Input validation and error handling
- Multiple implementations (baseline, optimized, generic)

## Build and Run

### Prerequisites
- Java 25+
- Maven 3.6+

### Building
```bash
mvn clean compile
```

### Running Tests
```bash
mvn test
```

### Running Benchmarks
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=benchmark"
```

### Running Correctness Tests
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=test"
```

### Single Test with Custom Size
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=single 1000"
```