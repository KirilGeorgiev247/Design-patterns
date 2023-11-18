package figures;

import exceptions.NegativeSideOrZeroNotAllowed;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class RectangleTest {

    static Rectangle validRectangle;

    static double delta = 0.00001;

    @BeforeEach
    void setUp() {
        validRectangle = new Rectangle(2, 3);
    }

    @Test
    void isInvalidRectangleExceptionThrownWhenSideIsNegative() {
        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Rectangle(-1, 2),
            "A side of the rectangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Rectangle(1, -2),
            "A side of the rectangle cannot be negative value!");
    }

    @Test
    void isInvalidRectangleExceptionThrownWhenSideIsZero() {
        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Rectangle(0, 2),
            "A side of the rectangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Rectangle(1, 0),
            "A side of the rectangle cannot be negative value!");
    }

    @Test
    void isPerimeterCalculationValid() {
        assertEquals(10, validRectangle.getPerimeter(), delta,
            "Perimeter calculation should be valid!");
    }

    @Test
    void areRectanglesWithTheSameSidesAndSameOrderEqual() {
        Rectangle sameValidRectangle = new Rectangle(2, 3);

        assertEquals(sameValidRectangle, validRectangle,
            "Rectangles with same sides and same order should be equal!");

        assertEquals(sameValidRectangle.getPerimeter(), validRectangle.getPerimeter(),
            "Rectangles with same sides and same order should have equal perimeters!");
    }

    @Test
    void areRectanglesWithTheSameSidesAndDiffOrderEqual() {
        Rectangle sameValidRectangle = new Rectangle(3, 2);

        assertEquals(sameValidRectangle, validRectangle,
            "Rectangles with same sides and different order should be equal!");

        assertEquals(sameValidRectangle.getPerimeter(), validRectangle.getPerimeter(),
            "Rectangles with same sides and different order should have equal perimeters!");
    }

    @Test
    void areRectanglesWithDiffSidesNotEqual() {
        Rectangle diffValidRectangle = new Rectangle(2, 3.0001);

        assertNotEquals(diffValidRectangle, validRectangle,
            "Rectangles with different sides should not be equal!");

        assertNotEquals(diffValidRectangle.getPerimeter(), validRectangle.getPerimeter(), delta,
            "Triangles with different sides should have different perimeters!");
    }

    @Test
    void isRectangleSameWithItself() {
        assertSame(true, validRectangle.equals(validRectangle),
            "Rectangle should be same with itself!");
    }

    @Test
    void testRectangleNotThrowingWhenCloned() {
        assertDoesNotThrow(() -> validRectangle.clone(),
            "Rectangle clone method should not throw!");
    }

    @Test
    void testRectangleCloneMethodShouldReturnEqualObject() throws CloneNotSupportedException{
        Figure rect2 = validRectangle.clone();

        assertNotEquals(null, rect2,
            "Cloned object should not be null!");

        assertEquals(validRectangle, rect2,
            "Prototype object should be equal to the rectangle!");

        assertEquals(validRectangle.getPerimeter(), rect2.getPerimeter(), delta,
            "Perimeters should be equal!");
    }

    @Test
    void testIfClonedRectangleReturnsSameToString() throws CloneNotSupportedException {
        Figure rect2 = validRectangle.clone();

        assertEquals(validRectangle.toString(), rect2.toString(),
            "Cloned rectangle should return the same stringification!");
    }

    @Test
    void testIfToStringMethodReturnsCorrectValue() {
        String expected = "Rectangle " + 3 + " " + 2;

        assertEquals(expected, validRectangle.toString(),
            "Stringification should return right value!");
    }

    @Test
    void testIfTwoRectanglesWithEqualSidesReturnSameStringification() {
        Rectangle secondRect = new Rectangle(2, 3);

        assertEquals(validRectangle.toString(), secondRect.toString(),
            "Rectangles with same sides should have equal stringification!");

        Rectangle thirdRect = new Rectangle(3, 2);

        assertEquals(validRectangle.toString(), thirdRect.toString(),
            "Rectangles with same sides and different order should have equal stringification!");
    }

    @Test
    void testIfTwoRectanglesWithEqualSidesReturnSameHashCode() {
        Rectangle secondRect = new Rectangle(2, 3);

        assertEquals(validRectangle.hashCode(), secondRect.hashCode(),
            "Rectangles with same sides should have equal hashCode!");

        Rectangle thirdRect = new Rectangle(3, 2);

        assertEquals(validRectangle.hashCode(), thirdRect.hashCode(),
            "Rectangles with same sides and different order should have equal hashCode!");
    }
}
