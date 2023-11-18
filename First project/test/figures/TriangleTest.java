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

public class TriangleTest {

    static Triangle validTriangle;

    static double delta = 0.00001;

    @BeforeEach
    void setUp() {
        validTriangle = new Triangle(1, 2, 3);
    }

    @Test
    void isInvalidTriangleExceptionThrownWhenSideIsNegative() {
        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(-1, 2, 3),
            "A side of the triangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(1, -2, 3),
            "A side of the triangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(1, 2, -3),
            "A side of the triangle cannot be negative value!");
    }

    @Test
    void isInvalidTriangleExceptionThrownWhenSideIsZero() {
        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(0, 2, 3),
            "A side of the triangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(1, 0, 3),
            "A side of the triangle cannot be negative value!");

        assertThrows(NegativeSideOrZeroNotAllowed.class,
            () -> new Triangle(1, 2, 0),
            "A side of the triangle cannot be negative value!");
    }

    @Test
    void isPerimeterCalculationValid() {
        assertEquals(6, validTriangle.getPerimeter(), delta,
            "Perimeter calculation should be valid!");
    }

    @Test
    void areTrianglesWithTheSameSidesAndSameOrderEqual() {
        Triangle sameValidTriangle = new Triangle(1, 2, 3);

        assertEquals(sameValidTriangle, validTriangle,
            "Triangles with same sides and same order should be equal!");

        assertEquals(sameValidTriangle.getPerimeter(), validTriangle.getPerimeter(),
            "Triangles with same sides and same order should have equal perimeters!");
    }

    @Test
    void areTrianglesWithTheSameSidesAndDiffOrderEqual() {
        Triangle sameValidTriangle = new Triangle(3, 1, 2);

        assertEquals(sameValidTriangle, validTriangle,
            "Triangles with same sides and different order should be equal!");

        assertEquals(sameValidTriangle.getPerimeter(), validTriangle.getPerimeter(),
            "Triangles with same sides and different order should have equal perimeters!");
    }

    @Test
    void areTrianglesWithDiffSidesNotEqual() {
        Triangle diffValidTriangle = new Triangle(1, 2, 3.0001);

        assertNotEquals(diffValidTriangle, validTriangle,
            "Triangles with different sides should not be equal!");

        assertNotEquals(diffValidTriangle.getPerimeter(), validTriangle.getPerimeter(), delta,
            "Triangles with different sides should have different perimeters!");
    }

    @Test
    void isTriangleSameWithItself() {
        assertSame(true, validTriangle.equals(validTriangle),
            "Triangle should be same with itself!");
    }

    @Test
    void testTriangleNotThrowingWhenCloned() {
        assertDoesNotThrow(() -> validTriangle.clone(),
            "Triangle clone method should not throw!");
    }

    @Test
    void testTriangleCloneMethodShouldReturnEqualObject() throws CloneNotSupportedException {
        Figure triangle2 = validTriangle.clone();

        assertNotEquals(null, triangle2,
            "Cloned object should not be null!");

        assertEquals(validTriangle, triangle2,
            "Prototype object should be equal to the triangle!");

        assertEquals(validTriangle.getPerimeter(), triangle2.getPerimeter(), delta,
            "Perimeters should be equal!");
    }

    @Test
    void testIfClonedTriangleHasTheSameToString() throws CloneNotSupportedException {
        Figure triangle2 = validTriangle.clone();

        assertEquals(validTriangle.toString(), triangle2.toString(),
            "Cloned triangle should return the same stringification!");
    }

    @Test
    void testIfToStringMethodReturnsCorrectValue() {
        String expected = "Triangle " + 3 + " " + 2 + " " + 1;

        assertEquals(expected, validTriangle.toString(),
            "Stringification should return right value!");
    }

    @Test
    void testIfTwoTrianglesWithEqualSidesReturnSameStringification() {
        Triangle secondTriangle = new Triangle(1, 2, 3);
        Triangle thirdTriangle = new Triangle(2, 3, 1);

        assertEquals(validTriangle.toString(), secondTriangle.toString(),
            "Triangles with same sides should have equal stringification!");

        assertEquals(validTriangle.toString(), thirdTriangle.toString(),
            "Triangles with same sides and different order should have equal stringification!");
    }

    @Test
    void testIfTwoTrianglesWithEqualSidesReturnSameHashCode() {
        Triangle secondTriangle = new Triangle(1, 2, 3);
        Triangle thirdTriangle = new Triangle(2, 3, 1);

        assertEquals(validTriangle.hashCode(), secondTriangle.hashCode(),
            "Triangles with same sides should have equal hashCode!");

        assertEquals(validTriangle.hashCode(), thirdTriangle.hashCode(),
            "Triangles with same sides and different order should have equal hashCode!");
    }
}
