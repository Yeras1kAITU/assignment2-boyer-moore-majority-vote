package cli;

import algorithms.BoyerMooreMajorityVote;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;

/**
 * JMH Microbenchmark for Boyer-Moore Majority Vote Algorithm
 * Run with: mvn clean package && java -jar target/microbenchmarks.jar
 */
@State(Scope.Benchmark)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@Fork(1)
public class JMHBenchmark {

    @Param({"1000", "10000", "100000"})
    private int arraySize;

    private int[] arrayWithMajority;
    private int[] arrayWithoutMajority;
    private int[] worstCaseArray;
    private BoyerMooreMajorityVote algorithm;

    @Setup(Level.Iteration)
    public void setup() {
        algorithm = new BoyerMooreMajorityVote();
        arrayWithMajority = generateArrayWithMajority(arraySize, 42);
        arrayWithoutMajority = generateArrayWithoutMajority(arraySize);
        worstCaseArray = generateWorstCaseArray(arraySize);
    }

    @Benchmark
    public Integer benchmarkWithMajority() {
        return algorithm.findMajorityElement(arrayWithMajority);
    }

    @Benchmark
    public Integer benchmarkWithoutMajority() {
        return algorithm.findMajorityElement(arrayWithoutMajority);
    }

    @Benchmark
    public Integer benchmarkWorstCase() {
        return algorithm.findMajorityElement(worstCaseArray);
    }

    @Benchmark
    public Integer benchmarkOptimizedWithMajority() {
        return algorithm.findMajorityElementOptimized(arrayWithMajority);
    }

    @Benchmark
    public Integer benchmarkEnhancedWithMajority() {
        return algorithm.findMajorityElementEnhanced(arrayWithMajority);
    }

    private int[] generateArrayWithMajority(int size, int majorityElement) {
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

    private int[] generateArrayWithoutMajority(int size) {
        int[] array = new int[size];
        var random = RandomGenerator.getDefault();

        for (int i = 0; i < size; i++) {
            array[i] = random.nextInt(size / 2);
        }

        return array;
    }

    private int[] generateWorstCaseArray(int size) {
        int[] array = new int[size];
        for (int i = 0; i < size; i++) {
            array[i] = i % 2;
        }
        return array;
    }

    /**
     * Main method to run JMH benchmarks directly
     */
    public static void main(String[] args) throws RunnerException {
        Options opt = new OptionsBuilder()
                .include(JMHBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(2)
                .measurementIterations(3)
                .timeUnit(TimeUnit.MILLISECONDS)
                .mode(Mode.AverageTime)
                .output("jmh-benchmark-results.txt")
                .build();

        new Runner(opt).run();
    }
}