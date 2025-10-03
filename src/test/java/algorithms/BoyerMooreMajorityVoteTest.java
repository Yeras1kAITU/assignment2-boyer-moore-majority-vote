package algorithms;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.Arguments;
import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;
import java.util.random.RandomGenerator;

/**
 * Comprehensive unit tests with edge cases and property-based testing
 */
class BoyerMooreMajorityVoteTest {

    private BoyerMooreMajorityVote bm;

    @BeforeEach
    void setUp() {
        bm = new BoyerMooreMajorityVote();
    }

    // Basic functionality tests
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

    // Edge cases
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

    // Input distribution tests
    @Test
    @DisplayName("Test sorted array with majority")
    void testSortedArrayWithMajority() {
        int[] nums = {1, 1, 1, 1, 2, 3, 4};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test reverse sorted array with majority")
    void testReverseSortedArrayWithMajority() {
        int[] nums = {4, 3, 2, 1, 1, 1, 1};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test nearly sorted array with majority")
    void testNearlySortedArrayWithMajority() {
        int[] nums = {1, 1, 2, 1, 1, 3, 4};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test alternating elements with majority")
    void testAlternatingElementsWithMajority() {
        int[] nums = {1, 2, 1, 2, 1, 2, 1};
        Integer result = bm.findMajorityElement(nums);
        assertEquals(1, result);
    }

    @Test
    @DisplayName("Test alternating elements without majority")
    void testAlternatingElementsWithoutMajority() {
        int[] nums = {1, 2, 1, 2, 1, 2};
        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    // Large array tests
    @Test
    @DisplayName("Test large array with majority")
    void testLargeArrayWithMajority() {
        int size = 10000;
        int[] nums = new int[size];
        java.util.Arrays.fill(nums, 0, size/2 + 1, 42);
        for (int i = size/2 + 1; i < size; i++) {
            nums[i] = i;
        }

        Integer result = bm.findMajorityElement(nums);
        assertEquals(42, result);
    }

    @Test
    @DisplayName("Test large array without majority")
    void testLargeArrayWithoutMajority() {
        int size = 10000;
        int[] nums = new int[size];
        for (int i = 0; i < size; i++) {
            nums[i] = i % 10; // No single element > 50%
        }

        Integer result = bm.findMajorityElement(nums);
        assertNull(result);
    }

    // Property-based tests
    @ParameterizedTest
    @MethodSource("provideRandomArrays")
    @DisplayName("Property test: if result exists, it must be majority")
    void testMajorityProperty(int[] array) {
        Integer result = bm.findMajorityElement(array);

        if (result != null) {
            int count = 0;
            for (int num : array) {
                if (num == result) count++;
            }
            assertTrue(count > array.length / 2,
                    "Result " + result + " is not majority in array of size " + array.length);
        }
    }

    @ParameterizedTest
    @MethodSource("provideMajorityArrays")
    @DisplayName("Property test: must find majority when it exists")
    void testMustFindMajority(int[] array, int expectedMajority) {
        Integer result = bm.findMajorityElement(array);
        assertNotNull(result, "Should find majority in array");
        assertEquals(expectedMajority, result);
    }

    private static Stream<Arguments> provideRandomArrays() {
        var random = RandomGenerator.getDefault();
        return Stream.generate(() -> {
            int size = random.nextInt(1000) + 1;
            int[] array = new int[size];
            for (int i = 0; i < size; i++) {
                array[i] = random.nextInt(size / 2 + 1);
            }
            return Arguments.of(array);
        }).limit(50); // Run 50 random tests
    }

    private static Stream<Arguments> provideMajorityArrays() {
        var random = RandomGenerator.getDefault();
        return Stream.generate(() -> {
            int size = random.nextInt(500) + 10;
            int majorityElement = random.nextInt(100);
            int[] array = new int[size];

            // Fill majority
            int majorityCount = size / 2 + 1;
            for (int i = 0; i < majorityCount; i++) {
                array[i] = majorityElement;
            }

            // Fill rest with different elements
            for (int i = majorityCount; i < size; i++) {
                array[i] = majorityElement + 1 + random.nextInt(100);
            }

            // Shuffle
            for (int i = 0; i < size; i++) {
                int j = random.nextInt(size);
                int temp = array[i];
                array[i] = array[j];
                array[j] = temp;
            }

            return Arguments.of(array, majorityElement);
        }).limit(20); // Run 20 majority tests
    }

    // Performance metric tests
    @Test
    @DisplayName("Test performance metrics collection")
    void testPerformanceMetrics() {
        BoyerMooreMajorityVote bmWithMetrics = new BoyerMooreMajorityVote(true);
        int[] nums = {1, 2, 3, 2, 2, 2, 1};

        Integer result = bmWithMetrics.findMajorityElement(nums);
        assertEquals(2, result);

        var tracker = bmWithMetrics.getPerformanceTracker();
        assertNotNull(tracker);
        assertTrue(tracker.getComparisonCount() > 0);
        assertTrue(tracker.getArrayAccessCount() > 0);
    }

    // Algorithm variants tests
    @Test
    @DisplayName("Test optimized version")
    void testOptimizedVersion() {
        int[] nums = {1, 2, 3, 2, 2, 2, 1};
        Integer result = bm.findMajorityElementOptimized(nums);
        assertEquals(2, result);
    }

    @Test
    @DisplayName("Test enhanced version with edge cases")
    void testEnhancedVersion() {
        // Single element
        assertEquals(5, bm.findMajorityElementEnhanced(new int[]{5}));

        // Two same elements
        assertEquals(3, bm.findMajorityElementEnhanced(new int[]{3, 3}));

        // Two different elements
        assertNull(bm.findMajorityElementEnhanced(new int[]{1, 2}));
    }

    @Test
    @DisplayName("Test generic version")
    void testGenericVersion() {
        String[] strings = {"apple", "banana", "apple", "apple", "cherry"};
        String result = BoyerMooreMajorityVote.findMajorityElementGeneric(strings);
        assertEquals("apple", result);
    }

    @Test
    @DisplayName("Test safe version with invalid input")
    void testSafeVersion() {
        var result1 = bm.findMajorityElementSafe(null);
        assertTrue(result1.isEmpty());

        var result2 = bm.findMajorityElementSafe(new int[0]);
        assertTrue(result2.isEmpty());

        var result3 = bm.findMajorityElementSafe(new int[]{1, 2, 2, 2, 3});
        assertTrue(result3.isPresent());
        assertEquals(2, result3.get());
    }
}