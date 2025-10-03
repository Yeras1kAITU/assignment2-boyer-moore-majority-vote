package cli;

import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import java.util.random.RandomGenerator;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

/**
 * Comprehensive Benchmark Runner for Boyer-Moore Majority Vote Algorithm
 */
public class BenchmarkRunner {

    private static final String RESULTS_DIR = "benchmark-results";
    private static final String CSV_HEADER = "ArraySize,InputType,HasMajority,Time(ns),Comparisons,ArrayAccess,MemoryAllocations";

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        try {
            Files.createDirectories(Paths.get(RESULTS_DIR));
        } catch (IOException e) {
            System.err.println("Warning: Could not create results directory: " + e.getMessage());
        }

        switch (args[0]) {
            case "benchmark" -> runComprehensiveBenchmarks();
            case "jmh" -> runJMHBenchmarks();
            case "test" -> runCorrectnessTests();
            case "property-test" -> runPropertyBasedTests();
            case "distribution-test" -> runDistributionTests();
            case "single" -> {
                if (args.length < 2) {
                    System.out.println("Please provide array size for single test");
                    return;
                }
                runSingleTest(Integer.parseInt(args[1]));
            }
            case "plot" -> generatePlots();
            default -> printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("""
            Boyer-Moore Majority Vote Benchmark CLI
            Usage:
              benchmark       - Run comprehensive benchmarks with CSV export
              jmh             - Run JMH microbenchmarks
              test            - Run correctness tests
              property-test   - Run property-based testing
              distribution-test - Test different input distributions
              single <size>   - Run single test with given array size
              plot            - Generate performance plots (requires Python)
            """);
    }

    private static void runComprehensiveBenchmarks() {
        System.out.println("Running Comprehensive Benchmarks with CSV Export");

        String csvFile = RESULTS_DIR + "/benchmark_results.csv";
        List<String> results = new ArrayList<>();
        results.add(CSV_HEADER);

        int[] sizes = {100, 500, 1000, 5000, 10000, 50000, 100000};
        String[] distributions = {"RANDOM", "SORTED", "REVERSE_SORTED", "NEARLY_SORTED"};

        for (int size : sizes) {
            System.out.printf("Testing size: %,d%n", size);

            for (String distribution : distributions) {
                // Test with majority
                var result1 = benchmarkDistribution(size, distribution, true);
                results.add(formatCSVResult(size, distribution, true, result1));

                // Test without majority
                var result2 = benchmarkDistribution(size, distribution, false);
                results.add(formatCSVResult(size, distribution, false, result2));
            }

            // Test worst case
            var worstCaseResult = benchmarkWorstCase(size);
            results.add(formatCSVResult(size, "WORST_CASE", true, worstCaseResult));
        }

        // Write CSV file
        try (FileWriter writer = new FileWriter(csvFile)) {
            for (String line : results) {
                writer.write(line + "\n");
            }
            System.out.println("Results saved to: " + csvFile);
        } catch (IOException e) {
            System.err.println("Error writing CSV: " + e.getMessage());
        }

        generateCSVSummary(results);
    }

    private static BenchmarkResult benchmarkDistribution(int size, String distribution, boolean withMajority) {
        int[] array = generateArrayWithDistribution(size, distribution, withMajority);
        var bm = new BoyerMooreMajorityVote(true);

        long startTime = System.nanoTime();
        Integer result = bm.findMajorityElement(array);
        long endTime = System.nanoTime();

        var tracker = bm.getPerformanceTracker();
        return new BenchmarkResult(
                endTime - startTime,
                tracker.getComparisonCount(),
                tracker.getArrayAccessCount(),
                tracker.getAllMetrics().get("memoryAllocations"),
                result != null
        );
    }

    private static BenchmarkResult benchmarkWorstCase(int size) {
        int[] array = generateWorstCaseArray(size);
        var bm = new BoyerMooreMajorityVote(true);

        long startTime = System.nanoTime();
        Integer result = bm.findMajorityElement(array);
        long endTime = System.nanoTime();

        var tracker = bm.getPerformanceTracker();
        return new BenchmarkResult(
                endTime - startTime,
                tracker.getComparisonCount(),
                tracker.getArrayAccessCount(),
                tracker.getAllMetrics().get("memoryAllocations"),
                result != null
        );
    }

    private static String formatCSVResult(int size, String distribution, boolean hasMajority, BenchmarkResult result) {
        return String.format("%d,%s,%b,%d,%d,%d,%d",
                size, distribution, hasMajority, result.timeNs,
                result.comparisons, result.arrayAccess, result.memoryAllocations);
    }

    private static void generateCSVSummary(List<String> results) {
        String summaryFile = RESULTS_DIR + "/benchmark_summary.csv";
        try (FileWriter writer = new FileWriter(summaryFile)) {
            writer.write("Size,AvgTime(ns),AvgComparisons,AvgArrayAccess\n");

            // Group by size and calculate averages
            int[] sizes = {100, 500, 1000, 5000, 10000, 50000, 100000};
            for (int size : sizes) {
                long totalTime = 0, totalComparisons = 0, totalAccess = 0;
                int count = 0;

                for (int i = 1; i < results.size(); i++) { // Skip header
                    String[] parts = results.get(i).split(",");
                    if (Integer.parseInt(parts[0]) == size) {
                        totalTime += Long.parseLong(parts[3]);
                        totalComparisons += Long.parseLong(parts[4]);
                        totalAccess += Long.parseLong(parts[5]);
                        count++;
                    }
                }

                if (count > 0) {
                    writer.write(String.format("%d,%.2f,%.2f,%.2f\n",
                            size, (double)totalTime/count,
                            (double)totalComparisons/count, (double)totalAccess/count));
                }
            }
            System.out.println("Summary saved to: " + summaryFile);
        } catch (IOException e) {
            System.err.println("Error writing summary: " + e.getMessage());
        }
    }

    private static int[] generateArrayWithDistribution(int size, String distribution, boolean withMajority) {
        return switch (distribution) {
            case "SORTED" -> generateSortedArray(size, withMajority);
            case "REVERSE_SORTED" -> generateReverseSortedArray(size, withMajority);
            case "NEARLY_SORTED" -> generateNearlySortedArray(size, withMajority);
            default -> withMajority ?
                    generateArrayWithMajority(size, 42) :
                    generateArrayWithoutMajority(size);
        };
    }

    private static int[] generateSortedArray(int size, boolean withMajority) {
        int[] array = new int[size];
        if (withMajority) {
            int majority = size / 2;
            for (int i = 0; i < size; i++) {
                array[i] = i < majority ? 42 : 42 + 1 + i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                array[i] = i;
            }
        }
        return array;
    }

    private static int[] generateReverseSortedArray(int size, boolean withMajority) {
        int[] array = new int[size];
        if (withMajority) {
            int majority = size / 2;
            for (int i = 0; i < size; i++) {
                array[i] = i < majority ? 42 : size - i;
            }
        } else {
            for (int i = 0; i < size; i++) {
                array[i] = size - i;
            }
        }
        return array;
    }

    private static int[] generateNearlySortedArray(int size, boolean withMajority) {
        int[] array = generateSortedArray(size, withMajority);
        var random = RandomGenerator.getDefault();

        // Swap some elements to make it nearly sorted
        for (int i = 0; i < size / 10; i++) {
            int idx1 = random.nextInt(size);
            int idx2 = random.nextInt(size);
            int temp = array[idx1];
            array[idx1] = array[idx2];
            array[idx2] = temp;
        }
        return array;
    }

    private static int[] generateArrayWithMajority(int size, int majorityElement) {
        int[] array = new int[size];
        int majorityCount = size / 2 + 1;
        var random = RandomGenerator.getDefault();

        for (int i = 0; i < majorityCount; i++) {
            array[i] = majorityElement;
        }

        for (int i = majorityCount; i < size; i++) {
            array[i] = majorityElement + 1 + random.nextInt(100);
        }

        for (int i = 0; i < size; i++) {
            int j = random.nextInt(size);
            int temp = array[i];
            array[i] = array[j];
            array[j] = temp;
        }

        return array;
    }

    private static int[] generateArrayWithoutMajority(int size) {
        int[] array = new int[size];
        var random = RandomGenerator.getDefault();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size / 2);
        }

        return array;
    }

    private static int[] generateWorstCaseArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i % 2;
        }
        return array;
    }

    private static void generatePlots() {
        System.out.println("""
            To generate performance plots:
            
            Option 1: Use the provided Python script
            python plot_performance.py
            
            Option 2: Manual plotting with CSV data:
            - Load benchmark_results.csv into Excel, Google Sheets, or Python
            - Create line charts with ArraySize on x-axis and metrics on y-axis
            - Compare different distributions and majority scenarios
            
            Sample Python code:
            ```python
            import pandas as pd
            import matplotlib.pyplot as plt
            
            df = pd.read_csv('benchmark-results/benchmark_results.csv')
            df.groupby('ArraySize')['Time(ns)'].mean().plot(
                title='Time vs Array Size', 
                logx=True, 
                logy=True
            )
            plt.show()
            ```
            """);
    }

    private static void runCorrectnessTests() {
        System.out.println("Running Correctness Tests...");

        var bm = new BoyerMooreMajorityVote();

        int[][] testCases = {
                {1, 2, 3, 2, 2, 2, 1},
                {1, 2, 3, 4, 5},
                {1},
                {2, 2},
                {1, 2},
                {3, 3, 4, 2, 4, 4, 2, 4, 4}
        };

        Integer[] expected = {2, null, 1, 2, null, 4};

        boolean allPassed = true;
        for (int i = 0; i < testCases.length; i++) {
            Integer result = bm.findMajorityElement(testCases[i]);
            boolean passed = (result == null && expected[i] == null) ||
                    (result != null && result.equals(expected[i]));

            System.out.printf("Test %d: %s - Expected: %s, Got: %s%n",
                    i + 1, passed ? "PASS" : "FAIL", expected[i], result);

            if (!passed) {
                allPassed = false;
            }
        }

        System.out.println("\nOverall: " + (allPassed ? "ALL TESTS PASSED" : "SOME TESTS FAILED"));
    }

    private static void runSingleTest(int size) {
        System.out.printf("Running single test with size %d%n", size);

        int[] array = generateArrayWithMajority(size, 99);
        var bm = new BoyerMooreMajorityVote(true);

        long startTime = System.nanoTime();
        Integer result = bm.findMajorityElement(array);
        long endTime = System.nanoTime();

        System.out.printf("Result: %s%n", result);
        System.out.printf("Execution Time: %,d ns%n", endTime - startTime);
        bm.getPerformanceTracker().printMetrics();
    }

    /**
     * Run JMH Microbenchmarks - Inform user about proper usage
     */
    private static void runJMHBenchmarks() {
        System.out.println("""
            JMH Microbenchmarks require a two-step process:
            
            Step 1: Package the application with JMH
            mvn clean package
            
            Step 2: Run the JMH benchmarks
            java -jar target/microbenchmarks.jar
            
            Alternatively, run the JMH benchmark directly:
            mvn compile exec:java -Dexec.mainClass="cli.JMHBenchmark"
            
            This will generate detailed performance measurements in jmh-benchmark-results.txt
            """);
    }

    /**
     * Property-based testing for algorithm correctness
     */
    private static void runPropertyBasedTests() {
        System.out.println("Running Property-Based Tests...");
        var bm = new BoyerMooreMajorityVote();
        var random = RandomGenerator.getDefault();
        int testsPassed = 0;
        int totalTests = 1000;

        for (int i = 0; i < totalTests; i++) {
            int size = random.nextInt(1000) + 1;
            int[] array = generateRandomArray(size, random);

            Integer result = bm.findMajorityElement(array);

            // Property 1: If result is not null, it must appear more than n/2 times
            if (result != null) {
                int count = 0;
                for (int num : array) {
                    if (num == result) count++;
                }
                if (count <= size / 2) {
                    System.out.printf("FAIL: Property 1 violated for array size %d%n", size);
                    continue;
                }
            }

            // Property 2: Algorithm should always find majority if it exists
            // Manually check if majority exists
            boolean majorityExists = false;
            for (int j = 0; j < size; j++) {
                int candidate = array[j];
                int count = 0;
                for (int num : array) {
                    if (num == candidate) count++;
                }
                if (count > size / 2) {
                    majorityExists = true;
                    if (result == null || result != candidate) {
                        System.out.printf("FAIL: Property 2 violated - missed majority%n");
                        break;
                    }
                }
            }

            // Property 3: If no majority exists, result must be null
            if (!majorityExists && result != null) {
                System.out.printf("FAIL: Property 3 violated - false positive%n");
                continue;
            }

            testsPassed++;
        }

        System.out.printf("Property-Based Tests: %d/%d passed (%.1f%%)%n",
                testsPassed, totalTests, (testsPassed * 100.0 / totalTests));
    }

    /**
     * Test different input distributions
     */
    private static void runDistributionTests() {
        System.out.println("Running Input Distribution Tests...");
        var bm = new BoyerMooreMajorityVote();

        String[] distributions = {
                "RANDOM", "SORTED", "REVERSE_SORTED", "NEARLY_SORTED", "ALL_SAME", "ALTERNATING"
        };

        boolean allPassed = true;
        for (String dist : distributions) {
            System.out.printf("Testing %s distribution... ", dist);
            boolean passed = testDistribution(bm, dist, 1000);
            System.out.println(passed ? "PASS" : "FAIL");
            if (!passed) allPassed = false;
        }

        System.out.println("Distribution Tests: " + (allPassed ? "ALL PASSED" : "SOME FAILED"));
    }

    private static boolean testDistribution(BoyerMooreMajorityVote bm, String distribution, int size) {
        int[] array = generateDistributionArray(distribution, size, true);
        Integer result = bm.findMajorityElement(array);

        // Verify the result is correct
        if (result == null) return false;

        int count = 0;
        for (int num : array) {
            if (num == result) count++;
        }
        return count > size / 2;
    }

    private static int[] generateDistributionArray(String distribution, int size, boolean withMajority) {
        return switch (distribution) {
            case "SORTED" -> generateSortedArray(size, withMajority);
            case "REVERSE_SORTED" -> generateReverseSortedArray(size, withMajority);
            case "NEARLY_SORTED" -> generateNearlySortedArray(size, withMajority);
            case "ALL_SAME" -> {
                int[] array = new int[size];
                java.util.Arrays.fill(array, 42);
                yield array;
            }
            case "ALTERNATING" -> {
                int[] array = new int[size];
                for (int i = 0; i < size; i++) {
                    array[i] = i % 2;
                }
                yield array;
            }
            default -> withMajority ?
                    generateArrayWithMajority(size, 42) :
                    generateArrayWithoutMajority(size);
        };
    }

    private static int[] generateRandomArray(int size, RandomGenerator random) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size / 2 + 1); // High chance of duplicates
        }
        return array;
    }

    // Helper class for benchmark results
    private static class BenchmarkResult {
        final long timeNs;
        final long comparisons;
        final long arrayAccess;
        final long memoryAllocations;
        final boolean hasMajority;

        BenchmarkResult(long timeNs, long comparisons, long arrayAccess, long memoryAllocations, boolean hasMajority) {
            this.timeNs = timeNs;
            this.comparisons = comparisons;
            this.arrayAccess = arrayAccess;
            this.memoryAllocations = memoryAllocations;
            this.hasMajority = hasMajority;
        }
    }
}