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
}