package figures;

import exceptions.NegativeSideOrZeroNotAllowed;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Objects;

public class Rectangle implements Figure {

    private double sideA;
    private double sideB;
    private double perimeter;

    private DecimalFormat df;

    public Rectangle(double sideA, double sideB) {
        if (Double.compare(sideA, 0.0) <= 0 || Double.compare(sideB, 0.0) <= 0) {
            throw new NegativeSideOrZeroNotAllowed();
        }

        df = new DecimalFormat("#.####");

        this.sideA = Math.max(sideA, sideB);
        this.sideB = sideA + sideB - this.sideA;
        perimeter = 2 * (sideA + sideB);
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

        return MessageFormat.format("Rectangle {0} {1}", df.format(sideA), df.format(sideB));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Rectangle rectangle = (Rectangle) o;
        return Double.compare(sideA, rectangle.sideA) == 0 && Double.compare(sideB, rectangle.sideB) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(perimeter);
    }
}
