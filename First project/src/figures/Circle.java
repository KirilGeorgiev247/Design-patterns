package figures;

import exceptions.NegativeRadiusOrZeroNotAllowed;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Objects;

public class Circle implements Figure {

    private double radius;
    private double perimeter;
    private Point center;

    private DecimalFormat df;

    public Circle(double radius, Point center) {
        if (Double.compare(radius, 0.0) <= 0) {
            throw new NegativeRadiusOrZeroNotAllowed();
        }

        df = new DecimalFormat("#.####");

        this.radius = radius;
        this.center = center;
        perimeter = 2 * Math.PI * radius;
    }

    @Override
    public double getPerimeter() {
        return perimeter;
    }

    @Override
    public Figure clone() throws CloneNotSupportedException {
        return (Figure) super.clone();
    }

    @Override
    public String toString() {

        return MessageFormat.format("Circle {0} ({1}, {2})",
            df.format(radius), df.format(center.x()), df.format(center.y()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Circle circle = (Circle) o;
        return Double.compare(radius, circle.radius) == 0 && Objects.equals(center, circle.center);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(perimeter);
    }
}
