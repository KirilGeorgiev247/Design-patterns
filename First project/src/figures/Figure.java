package figures;

public interface Figure extends Cloneable {

    double getPerimeter();

    Figure clone() throws CloneNotSupportedException;

}
