package cli;

import algorithms.BoyerMooreMajorityVote;
import metrics.PerformanceTracker;
import java.util.random.RandomGenerator;

/**
 * Command-line interface for benchmarking Boyer-Moore Majority Vote algorithm
 */
public class BenchmarkRunner {

    public static void main(String[] args) {
        if (args.length == 0) {
            printUsage();
            return;
        }

        switch (args[0]) {
            case "benchmark" -> runBenchmarks();
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
              benchmark - Run comprehensive benchmarks
              test      - Run correctness tests
              single <size> - Run single test with given array size
            """);
    }

    private static void runBenchmarks() {
        System.out.println("Running Boyer-Moore Majority Vote Benchmarks");
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

        System.out.println(tracker.getMetricsCSV() + ",true");
    }

    private static void benchmarkWithoutMajority(int size) {
        int[] array = generateArrayWithoutMajority(size);
        var bm = new BoyerMooreMajorityVote(true);

        Integer result = bm.findMajorityElement(array);
        var tracker = bm.getPerformanceTracker();

        System.out.println(tracker.getMetricsCSV() + ",false");
    }

    private static void benchmarkWorstCase(int size) {
        int[] array = generateWorstCaseArray(size);
        var bm = new BoyerMooreMajorityVote(true);

        Integer result = bm.findMajorityElement(array);
        var tracker = bm.getPerformanceTracker();

        System.out.println(tracker.getMetricsCSV() + ",worst");
    }

    private static int[] generateArrayWithMajority(int size, int majorityElement) {
        int[] array = new int[size];
        int majorityCount = size / 2 + 1;
        var random = RandomGenerator.getDefault();

        // Fill majority elements
        for (int i = 0; i < majorityCount; i++) {
            array[i] = majorityElement;
        }

        // Fill remaining with random elements
        for (int i = majorityCount; i < size; i++) {
            array[i] = majorityElement + 1 + random.nextInt(100);
        }

        // Shuffle the array
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
            array[i] = random.nextInt(size / 2); // Ensure no majority
        }

        return array;
    }

    private static int[] generateWorstCaseArray(int size) {
        int[] array = new int[size];
        // Worst case: alternating elements that cause maximum count fluctuations
        for (int i = 0; i < size; i++) {
            array[i] = i % 2;
        }
        return array;
    }

    private static void runCorrectnessTests() {
        System.out.println("Running Correctness Tests...");

        var bm = new BoyerMooreMajorityVote();

        int[][] testCases = {
                {1, 2, 3, 2, 2, 2, 1},  // Majority exists
                {1, 2, 3, 4, 5},         // No majority
                {1},                      // Single element
                {2, 2},                   // Two same elements
                {1, 2},                   // Two different elements
                {3, 3, 4, 2, 4, 4, 2, 4, 4}  // Majority exists
        };

        Integer[] expected = {2, null, 1, 2, null, 4};

        boolean allPassed = true;
        for (int i = 0; i < testCases.length; i++) {
            Integer result = bm.findMajorityElement(testCases[i]);
            boolean passed = (result == null && expected[i] == null) ||
                    (result != null && result.equals(expected[i]));

            System.out.printf("Test %d: %s - Expected: %s, Got: %s%n",
                    i + 1,
                    passed ? "PASS" : "FAIL",
                    expected[i],
                    result);

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
}