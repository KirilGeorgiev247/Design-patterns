package figures;

import exceptions.NegativeRadiusOrZeroNotAllowed;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.text.MessageFormat;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircleTest {

    static double delta = 0.00001;
    @Mock
    private Point pointMock = new Point(1, 2);

    @Test
    void isNegativeRadiusThrowing() {
        assertThrows(NegativeRadiusOrZeroNotAllowed.class,
            () -> new Circle(-1, pointMock),
            "Negative radius should throw!");
    }

    @Test
    void isZeroRadiusThrowing() {
        assertThrows(NegativeRadiusOrZeroNotAllowed.class,
            () -> new Circle(0, pointMock),
            "Radius zero means one point, which is not a valid circle and should throws!");
    }

    @Test
    void isPerimeterCalculationValid() {
        assertEquals(Math.PI * 2 * 4, new Circle(4, pointMock).getPerimeter(),
            delta,
            "Perimeter calculation should be valid by integers!");

        assertEquals(Math.PI * 2 * 0.123, new Circle(0.123, pointMock).getPerimeter(),
            delta,
            "Perimeter calculation should be valid by doubles!");

        assertEquals(Math.PI * 2 * 4.00000000001, new Circle(4.0000001, pointMock).getPerimeter(),
            delta,
            "Perimeter calculation should be valid by almost equals doubles!");
    }

    @Test
    void isCircleWithDiffRadiusNotEqual() {
        assertNotEquals(new Circle(5, pointMock), new Circle(4.999, pointMock),
            "Circles with different radius should be different!");
    }

    @Test
    void isCircleWithSameRadiusAndSameCenterEqual() {
        assertEquals(new Circle(5, pointMock), new Circle(5, pointMock),
            "Circles with same radius and same center should be equal!");
    }

    @Test
    void isCircleWithSameRadiusAndDiffCenterNotEqual() {
        assertNotEquals(new Circle(5, pointMock), new Circle(5, new Point(0, 0)),
            "Circles with same radius and different center should be different!");
    }

    @Test
    void isCircleSameWithItself() {
        Circle validCircle = new Circle(2, pointMock);
        assertTrue(validCircle.equals(validCircle),
            "Circle should be equal to itself!");
    }

    @Test
    void testCircleNotThrowingWhenCloned() {
        Circle circle = new Circle(5, pointMock);

        assertDoesNotThrow(() -> circle.clone(),
            "Circle clone method should not throw!");
    }

    @Test
    void testIfCircleCloneMethodReturnsEqualObject() throws CloneNotSupportedException {
        Circle circle = new Circle(5, pointMock);
        Figure circle2 = circle.clone();

        assertNotEquals(null, circle2,
            "Cloned object should not be null!");

        assertEquals(circle, circle2,
            "Prototype object should be equal to the circle!");

        assertEquals(circle.getPerimeter(), circle2.getPerimeter(), delta,
            "Perimeters should be equal!");
    }

    @Test
    void testIfClonedCircleReturnsSameToString() throws CloneNotSupportedException {
        Circle circle = new Circle(5, pointMock);
        Figure circle2 = circle.clone();

        assertEquals(circle.toString(), circle2.toString(),
            "Cloned circle should return the same stringification!");
    }

    @Test
    void testIfToStringMethodReturnsCorrectValue() {
        Circle circle = new Circle(5, pointMock);
        String expected = MessageFormat.format("Circle {0} ({1}, {2})",
            5, pointMock.x(), pointMock.y());

        assertEquals(expected, circle.toString(),
            "Stringification should return right value!");
    }

    @Test
    void testIfTwoCirclesWithEqualRadiusReturnSameStringification() {
        Circle firstCircle = new Circle(5, pointMock);
        Circle secondCircle = new Circle(5, pointMock);

        assertEquals(firstCircle.toString(), secondCircle.toString(),
            "Circles with same radius should have equal stringification!");
    }

    @Test
    void testIfTwoCirclesWithEqualRadiusReturnSameHashCode() {
        Circle firstCircle = new Circle(5, pointMock);
        Circle secondCircle = new Circle(5, pointMock);

        assertEquals(firstCircle.hashCode(), secondCircle.hashCode(),
            "Circles with same radius should have equal hashCode!");
    }
}
