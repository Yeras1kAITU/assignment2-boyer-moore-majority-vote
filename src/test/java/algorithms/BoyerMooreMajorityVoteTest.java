package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for BoyerMooreMajorityVote algorithm
 */
class BoyerMooreMajorityVoteTest {

    private BoyerMooreMajorityVote bm;

    @BeforeEach
    void setUp() {
        bm = new BoyerMooreMajorityVote();
    }

    @Test
    @DisplayName("Test with majority element present")
    void testWithMajorityElement() {
        int[] nums = {1, 2, 3, 2, 2, 2, 1};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Test without majority element")
    void testWithoutMajorityElement() {
        int[] nums = {1, 2, 3, 4, 5};
        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    @Test
    @DisplayName("Test single element array")
    void testSingleElement() {
        int[] nums = {5};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Test empty array throws exception")
    void testEmptyArray() {
        int[] nums = {};
        assertThrows(IllegalArgumentException.class, () -> bm.findMajorityElement(nums));
    }

    @Test
    @DisplayName("Test null array throws exception")
    void testNullArray() {
        assertThrows(IllegalArgumentException.class, () -> bm.findMajorityElement(null));
    }

    @Test
    @DisplayName("Test two same elements")
    void testTwoSameElements() {
        int[] nums = {3, 3};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Test two different elements")
    void testTwoDifferentElements() {
        int[] nums = {1, 2};
        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    @Test
    @DisplayName("Test all same elements")
    void testAllSameElements() {
        int[] nums = {4, 4, 4, 4, 4};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(4, result);
    }

    @Test
    @DisplayName("Test with exactly half elements")
    void testExactlyHalfElements() {
        int[] nums = {1, 1, 1, 2, 2, 2};
        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    @Test
    @DisplayName("Test performance metrics collection")
    void testPerformanceMetrics() {
        BoyerMooreMajorityVote bmWithMetrics = new BoyerMooreMajorityVote(true);
        int[] nums = {1, 2, 3, 2, 2, 2, 1};

        Integer result = bmWithMetrics.findMajorityElement(nums);
        assertEquals(2, result);

        assertNotNull(bmWithMetrics.getPerformanceTracker());
    }

    @Test
    @DisplayName("Test optimized version")
    void testOptimizedVersion() {
        int[] nums = {1, 2, 3, 2, 2, 2, 1};
        Integer result = bm.findMajorityElementOptimized(nums);
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Test generic version with strings")
    void testGenericVersion() {
        String[] strings = {"apple", "banana", "apple", "apple", "cherry"};
        String result = BoyerMooreMajorityVote.findMajorityElementGeneric(strings);
        assertEquals("apple", result);
    }

    @Test
    @DisplayName("Test generic version with no majority")
    void testGenericVersionNoMajority() {
        String[] strings = {"apple", "banana", "cherry", "date"};
        String result = BoyerMooreMajorityVote.findMajorityElementGeneric(strings);
        assertNull(result);
    }

    @Test
    @DisplayName("Test worst-case scenario")
    void testWorstCaseScenario() {
        int[] nums = {1, 2, 1, 2, 1, 2, 1};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test large array with majority")
    void testLargeArray() {
        int size = 10000;
        int[] nums = new int[size];
        java.util.Arrays.fill(nums, 0, size/2 + 1, 42); // Majority
        for (int i = size/2 + 1; i < size; i++) {
            nums[i] = i; // Distinct elements
        }

        Integer result = bm.findMajorityElement(nums);
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Test enhanced version with single element")
    void testEnhancedVersionSingleElement() {
        int[] nums = {7};
        Integer result = bm.findMajorityElementEnhanced(nums);
        assertEquals(7, result);
    }

    @Test
    @DisplayName("Test enhanced version with two same elements")
    void testEnhancedVersionTwoSame() {
        int[] nums = {3, 3};
        Integer result = bm.findMajorityElementEnhanced(nums);
        assertEquals(3, result);
    }

    @Test
    @DisplayName("Test enhanced version with two different elements")
    void testEnhancedVersionTwoDifferent() {
        int[] nums = {1, 2};
        Integer result = bm.findMajorityElementEnhanced(nums);
        assertNull(result);
    }

    @Test
    @DisplayName("Test safe version with valid input")
    void testSafeVersionValid() {
        int[] nums = {1, 2, 2, 2, 3};
        var result = bm.findMajorityElementSafe(nums);
        assertTrue(result.isPresent());
        assertEquals(2, result.get());
    }

    @Test
    @DisplayName("Test safe version with null input")
    void testSafeVersionNull() {
        var result = bm.findMajorityElementSafe(null);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test safe version with empty input")
    void testSafeVersionEmpty() {
        var result = bm.findMajorityElementSafe(new int[0]);
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("Test batch processing")
    void testBatchProcessing() {
        var arrays = java.util.List.of(
                new int[]{1, 2, 2, 2, 3},
                new int[]{3, 3, 3, 2, 2},
                new int[]{1, 2, 3, 4, 5}
        );

        var results = bm.findMajorityElements(arrays);
        assertEquals(2, results.size());
        assertTrue(results.contains(2));
        assertTrue(results.contains(3));
    }

    @Test
    @DisplayName("Test array with all identical elements")
    void testAllIdenticalElements() {
        int[] nums = {5, 5, 5, 5, 5, 5, 5};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(5, result);
    }

    @Test
    @DisplayName("Test array with one element different")
    void testOneElementDifferent() {
        int[] nums = {4, 4, 4, 4, 4, 4, 3};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(4, result);
    }

    @Test
    @DisplayName("Test array with just over half majority")
    void testJustOverHalfMajority() {
        int[] nums = new int[101];
        java.util.Arrays.fill(nums, 0, 51, 8); // 51 elements of 8
        java.util.Arrays.fill(nums, 51, 101, 9); // 50 elements of 9
        Integer result = bm.findMajorityElement(nums);
        assertEquals(8, result);
    }

    @Test
    @DisplayName("Test array with just under half majority")
    void testJustUnderHalfMajority() {
        int[] nums = new int[100];
        java.util.Arrays.fill(nums, 0, 49, 7); // 49 elements of 7
        java.util.Arrays.fill(nums, 49, 100, 8); // 51 elements of various 8+
        for (int i = 49; i < 100; i++) {
            nums[i] = 8 + (i % 10); // Make them different
        }
        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    @Test
    @DisplayName("Test very large array performance")
    void testVeryLargeArray() {
        int size = 1000000;
        int[] nums = new int[size];
        // Create array with clear majority
        int majority = 999;
        int majorityCount = size / 2 + 1;
        java.util.Arrays.fill(nums, 0, majorityCount, majority);
        for (int i = majorityCount; i < size; i++) {
            nums[i] = i; // All different
        }

        long startTime = System.nanoTime();
        Integer result = bm.findMajorityElement(nums);
        long endTime = System.nanoTime();

        assertEquals(majority, result);
        long duration = endTime - startTime;
        // Should complete in reasonable time (e.g., less than 1 second)
        assertTrue(duration < 1_000_000_000L, "Algorithm took too long: " + duration + " ns");
    }
}