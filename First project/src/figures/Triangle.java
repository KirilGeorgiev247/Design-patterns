package figures;

import exceptions.NegativeSideOrZeroNotAllowed;

import java.text.DecimalFormat;
import java.text.MessageFormat;
import java.util.Objects;

// TODO -> is valid triangle a + b > c ...
public class Triangle implements Figure {

    private double sideA;
    private double sideB;
    private double sideC;

    private double perimeter;

    private DecimalFormat df;

    public Triangle(double sideA, double sideB, double sideC) {
        if (Double.compare(sideA, 0.0) <= 0 || Double.compare(sideB, 0.0) <= 0 || Double.compare(sideC, 0.0) <= 0) {
            throw new NegativeSideOrZeroNotAllowed();
        }

        df = new DecimalFormat("#.####");

        perimeter = sideA + sideB + sideC;
        this.sideA = Math.max(sideA, Math.max(sideB, sideC));
        this.sideC = Math.min(sideC, Math.min(sideB, sideA));
        this.sideB = perimeter - this.sideA - this.sideC;
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
        return MessageFormat.format("Triangle {0} {1} {2}",
            df.format(this.sideA), df.format(this.sideB), df.format(this.sideC));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Triangle triangle = (Triangle) o;
        return Double.compare(sideA, triangle.sideA) == 0 && Double.compare(sideB, triangle.sideB) == 0 &&
            Double.compare(sideC, triangle.sideC) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(perimeter);
    }
}
