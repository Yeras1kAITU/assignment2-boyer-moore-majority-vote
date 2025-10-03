# Boyer-Moore Majority Vote Algorithm
**Done by: Yerassyl Ibrayev SE-2425**
## Overview
This project implements the Boyer-Moore Majority Vote algorithm for finding the majority element in a sequence (element appearing more than n/2 times) with O(n) time complexity and O(1) space complexity. The implementation includes comprehensive testing, benchmarking, and performance analysis capabilities.

## Algorithm Description
The algorithm works in two phases:
1. **Candidate Selection**: Traverse the array maintaining a candidate and count
2. **Candidate Verification**: Verify if the candidate appears more than n/2 times

## Complexity Analysis
- **Time Complexity**: Θ(n), O(n), Ω(n) - Always makes exactly 2 passes through the array
- **Space Complexity**: Θ(1), O(1), Ω(1) - Uses only constant extra space
- **Comparisons**: 2n comparisons in worst case
- **Memory**: O(1) auxiliary space (only stores candidate and count variables)

## Features
- Clean, documented Java implementation (Java 25)
- Comprehensive unit tests with JUnit 5
- Performance metrics collection (comparisons, array accesses, timing, memory)
- CLI interface for benchmarking with configurable input sizes
- JMH microbenchmarks for accurate performance measurements
- CSV export of benchmark results for analysis
- Python scripts for performance visualization
- Support for multiple data types (generics)
- Input validation and error handling
- Multiple implementations (baseline, optimized, generic, parallel)
- Enhanced edge case handling
- Property-based testing with random inputs
- Input distribution testing (random, sorted, reverse-sorted, nearly-sorted)


## Build and Run

### Prerequisites
- Java 25+
- Maven 3.6+
- Python 3.8+ (for plotting, optional)

### Building
```bash
mvn clean compile
```

## Running Tests
### Comprehensive Unit Tests
```bash
mvn test
```
### Property-Based Testing 
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=property-test"
```

### Input Distribution Testing
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=distribution-test"
```

## Performance Benchmarking
### Simple Benchmarks with CSV Export
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=benchmark"
```

### Running Correctness Tests
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=test"
```

### JMH Microbenchmarks (High Accuracy)
```bash
# Package the application with JMH
mvn clean package

# Run JMH benchmarks
java -jar target/microbenchmarks.jar
```

### Single Test with Custom Size
```bash
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=single 1000"
```

## Performance Visualization
### Generate Performance Plots
```bash
# First run benchmarks to generate CSV data
mvn exec:java "-Dexec.mainClass=cli.BenchmarkRunner" "-Dexec.args=benchmark"

# Then generate plots using Python
cd benchmark-results
python plot_performance.py
```

## Usage Examples
### Basic Usage
```java
BoyerMooreMajorityVote bm = new BoyerMooreMajorityVote();
int[] nums = {1, 2, 3, 2, 2, 2, 1};
Integer majority = bm.findMajorityElement(nums); // Returns 2
```

### With Performance Tracking
```java
BoyerMooreMajorityVote bm = new BoyerMooreMajorityVote(true);
int[] nums = {1, 2, 3, 2, 2, 2, 1};
Integer result = bm.findMajorityElement(nums);
bm.getPerformanceTracker().printMetrics();
```

### Safe Version (No Exceptions)
```java
var result = bm.findMajorityElementSafe(nums);
if (result.isPresent()) {
        System.out.println("Majority: " + result.get());
}
```

### Generic Version
```java
String[] words = {"apple", "banana", "apple", "apple", "cherry"};
String result = BoyerMooreMajorityVote.findMajorityElementGeneric(words); // Returns "apple"

Integer[] numbers = {1, 2, 1, 1, 3};
Integer majority = BoyerMooreMajorityVote.findMajorityElementGeneric(numbers); // Returns 1
```

### Parallel Processing for Large Arrays
```java
Integer result = bm.findMajorityElementParallel(largeArray); // Uses parallel verification
```

## Testing Coverage

### Edge Cases Handled
- ✅ Empty arrays (throws IllegalArgumentException)
- ✅ Null arrays (throws IllegalArgumentException)
- ✅ Single element arrays (returns the element)
- ✅ Two element arrays (returns element if same, null if different)
- ✅ Arrays with exactly n/2 elements (returns null)
- ✅ Very large arrays (optimized implementations)
- ✅ Arrays with all identical elements
- ✅ Arrays with alternating elements
- ✅ Arrays with just over/under half majority

### Input Distributions Tested
- **Random**: Randomly distributed elements
- **Sorted**: Elements in ascending order
- **Reverse Sorted**: Elements in descending order
- **Nearly Sorted**: Mostly sorted with some swaps
- **All Same**: All elements identical
- **Alternating**: Elements alternating between values
- **Worst Case**: Maximum count fluctuations

### Property-Based Testing
- **1000+ random test cases** generated automatically
- **Verification**: If algorithm returns a result, it must be actual majority
- **Completeness**: Algorithm must find majority when it exists
- **Correctness**: No false positives when no majority exists

## Performance Characteristics

### Theoretical Analysis
- **Best Case**: Θ(n) - array with majority at beginning
- **Worst Case**: Θ(n) - alternating elements causing maximum count fluctuations
- **Average Case**: Θ(n) - consistent linear performance
- **Space**: Always O(1) - only stores candidate and count variables

### Empirical Validation
The benchmarking system validates:
- **Linear time complexity** through log-log plots
- **Constant space usage** via memory tracking
- **Consistent performance** across different input distributions
- **Theoretical vs practical alignment** with correlation analysis

### Benchmark Metrics Collected
- **Execution Time**: Nanosecond precision timing
- **Comparisons**: Number of element comparisons
- **Array Accesses**: Memory access patterns
- **Memory Allocations**: Heap usage tracking
- **Statistical Analysis**: Mean, standard deviation, correlations

## Output Files

### Generated Files
- `benchmark-results/benchmark_results.csv` - Raw benchmark data
- `benchmark-results/benchmark_summary.csv` - Statistical summary
- `benchmark-results/performance_analysis.png` - Multi-panel performance plots
- `benchmark-results/theory_vs_practice.png` - Theoretical validation
- `jmh-benchmark-results.txt` - JMH detailed output

### CSV Format
```csv
ArraySize,InputType,HasMajority,Time(ns),Comparisons,ArrayAccess,MemoryAllocations
100,RANDOM,true,12500,198,396,0
100,SORTED,true,11800,198,396,0
...
```
## Performance Plots

The Python plotting script generates:

1. **Time Complexity Analysis** - Log-log scale showing O(n) behavior
2. **Comparison Count Analysis** - Operations counting validation
3. **Memory Access Patterns** - Array access efficiency
4. **Distribution Comparison** - Performance across input types
5. **Theoretical Validation** - O(n) theoretical vs actual performance

## License
Educational Use - Algorithm Analysis Assignment