package cli;

import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import java.util.random.RandomGenerator;

/**
 * Comprehensive Benchmark Runner for Boyer-Moore Majority Vote Algorithm
 * Includes both simple benchmarks and JMH microbenchmarks integration
 */
public class BenchmarkRunner {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        switch (args[0]) {
            case "benchmark" -> runSimpleBenchmarks();
            case "jmh" -> runJMHBenchmarks();
            case "test" -> runCorrectnessTests();
            case "single" -> {
                if (args.length < 2) {
                    System.out.println("Please provide array size for single test");
                    return;
                }
                runSingleTest(Integer.parseInt(args[1]));
            }
            default -> printUsage();
        }
    }

    private static void printUsage() {
        System.out.println("""
            Boyer-Moore Majority Vote Benchmark CLI
            Usage:
              benchmark - Run simple benchmarks with performance tracking
              jmh       - Run JMH microbenchmarks (requires package first)
              test      - Run correctness tests
              single <size> - Run single test with given array size
            
            JMH Usage:
              mvn clean package
              java -jar target/microbenchmarks.jar
            """);
    }

    private static void runSimpleBenchmarks() {
        System.out.println("Running Simple Benchmarks with Performance Tracking");
        System.out.println(PerformanceTracker.getCSVHeader());

        int[] sizes = {100, 1000, 10000, 100000, 1000000};

        for (int size : sizes) {
            benchmarkWithMajority(size);
            benchmarkWithoutMajority(size);
            benchmarkWorstCase(size);
        }
    }

    private static void benchmarkWithMajority(int size) {
        int[] array = generateArrayWithMajority(size, 42);
        var bm = new BoyerMooreMajorityVote(true);

        Integer result = bm.findMajorityElement(array);
        var tracker = bm.getPerformanceTracker();

        System.out.println(tracker.getMetricsCSV() + ",with_majority");
    }

    private static void benchmarkWithoutMajority(int size) {
        int[] array = generateArrayWithoutMajority(size);
        var bm = new BoyerMooreMajorityVote(true);

        Integer result = bm.findMajorityElement(array);
        var tracker = bm.getPerformanceTracker();

        System.out.println(tracker.getMetricsCSV() + ",without_majority");
    }

    private static void benchmarkWorstCase(int size) {
        int[] array = generateWorstCaseArray(size);
        var bm = new BoyerMooreMajorityVote(true);

        Integer result = bm.findMajorityElement(array);
        var tracker = bm.getPerformanceTracker();

        System.out.println(tracker.getMetricsCSV() + ",worst_case");
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
}