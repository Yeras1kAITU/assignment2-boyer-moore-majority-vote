package metrics;

import java.util.HashMap;
import java.util.Map;

/**
 * Performance tracking utility for algorithm metrics
 */
public class PerformanceTracker {
    private final String algorithmName;
    private final Map<String, Long> metrics;
    private long startTime;
    private boolean isTiming;

    public PerformanceTracker(String algorithmName) {
        this.algorithmName = algorithmName;
        this.metrics = new HashMap<>();
        initializeMetrics();
    }

    private void initializeMetrics() {
        metrics.put("comparisons", 0L);
        metrics.put("arrayAccess", 0L);
        metrics.put("executionTime", 0L);
        metrics.put("memoryAllocations", 0L);
        metrics.put("calls", 0L);
    }

    public void startTiming() {
        startTime = System.nanoTime();
        isTiming = true;
    }

    public void stopTiming() {
        if (isTiming) {
            long endTime = System.nanoTime();
            metrics.put("executionTime", endTime - startTime);
            isTiming = false;
        }
    }

    public void incrementComparisons(long count) {
        metrics.put("comparisons", metrics.get("comparisons") + count);
    }

    public void incrementArrayAccess(long count) {
        metrics.put("arrayAccess", metrics.get("arrayAccess") + count);
    }

    public void incrementMemoryAllocations(long count) {
        metrics.put("memoryAllocations", metrics.get("memoryAllocations") + count);
    }

    public void incrementCalls() {
        metrics.put("calls", metrics.get("calls") + 1);
    }

    public long getComparisonCount() {
        return metrics.get("comparisons");
    }

    public long getArrayAccessCount() {
        return metrics.get("arrayAccess");
    }

    public long getExecutionTime() {
        return metrics.get("executionTime");
    }

    public Map<String, Long> getAllMetrics() {
        return new HashMap<>(metrics);
    }

    public void reset() {
        initializeMetrics();
    }

    public void printMetrics() {
        System.out.println("=== Performance Metrics for " + algorithmName + " ===");
        System.out.printf("Comparisons: %,d%n", getComparisonCount());
        System.out.printf("Array Accesses: %,d%n", getArrayAccessCount());
        System.out.printf("Execution Time: %,d ns%n", getExecutionTime());
        System.out.printf("Memory Allocations: %,d%n", metrics.get("memoryAllocations"));
        System.out.printf("Method Calls: %,d%n", metrics.get("calls"));
    }

    public String getMetricsCSV() {
        return String.format("%s,%d,%d,%d,%d,%d",
                algorithmName,
                getComparisonCount(),
                getArrayAccessCount(),
                getExecutionTime(),
                metrics.get("memoryAllocations"),
                metrics.get("calls"));
    }

    public static String getCSVHeader() {
        return "Algorithm,Comparisons,ArrayAccess,Time(ns),MemoryAllocations,Calls";
    }
}