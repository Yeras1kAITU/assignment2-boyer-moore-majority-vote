package algorithms;

import metrics.PerformanceTracker;

/**
 * Boyer-Moore Majority Vote Algorithm Implementation
 * Finds the majority element in O(n) time with O(1) space
 */
public class BoyerMooreMajorityVote {

    private final PerformanceTracker performanceTracker;
    private final boolean enableMetrics;

    public BoyerMooreMajorityVote() {
        this(false);
    }

    public BoyerMooreMajorityVote(boolean enableMetrics) {
        this.enableMetrics = enableMetrics;
        if (enableMetrics) {
            this.performanceTracker = new PerformanceTracker("BoyerMooreMajorityVote");
        } else {
            this.performanceTracker = null;
        }
    }

    /**
     * Finds the majority element using Boyer-Moore algorithm
     * @param nums array of elements
     * @return the majority element if exists, null otherwise
     * @throws IllegalArgumentException if input array is null or empty
     */
    public Integer findMajorityElement(int[] nums) {
        if (nums == null) {
            throw new IllegalArgumentException("Input array cannot be null");
        }

        if (nums.length == 0) {
            throw new IllegalArgumentException("Input array cannot be empty");
        }

        if (enableMetrics && performanceTracker != null) {
            performanceTracker.incrementArrayAccess(nums.length);
            performanceTracker.startTiming();
        }

        // Phase 1: Find candidate
        Integer candidate = findCandidate(nums);

        // Phase 2: Verify candidate
        if (candidate != null && verifyCandidate(nums, candidate)) {
            if (enableMetrics && performanceTracker != null) {
                performanceTracker.stopTiming();
            }
            return candidate;
        }

        if (enableMetrics && performanceTracker != null) {
            performanceTracker.stopTiming();
        }
        return null;
    }

    private Integer findCandidate(int[] nums) {
        Integer candidate = null;
        int count = 0;

        for (int num : nums) {
            if (enableMetrics && performanceTracker != null) {
                performanceTracker.incrementComparisons(1);
                performanceTracker.incrementArrayAccess(1);
            }

            if (count == 0) {
                candidate = num;
                count = 1;
            } else if (candidate == num) {
                count++;
            } else {
                count--;
            }
        }

        return candidate;
    }

    private boolean verifyCandidate(int[] nums, int candidate) {
        int count = 0;

        for (int num : nums) {
            if (enableMetrics && performanceTracker != null) {
                performanceTracker.incrementComparisons(1);
                performanceTracker.incrementArrayAccess(1);
            }

            if (num == candidate) {
                count++;
            }
        }

        return count > nums.length / 2;
    }

    public PerformanceTracker getPerformanceTracker() {
        return performanceTracker;
    }

    public void resetMetrics() {
        if (performanceTracker != null) {
            performanceTracker.reset();
        }
    }
}