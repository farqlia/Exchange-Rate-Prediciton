package datasciencealgorithms.utils.point;

import java.awt.*;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.Objects;

public class Point {

    private LocalDate x;
    private BigDecimal y;
    public static final Point EMPTY_POINT = new Point(LocalDate.EPOCH, BigDecimal.ZERO);

    public Point(LocalDate x, BigDecimal y) {
        this.x = x;
        this.y = y.setScale(8, RoundingMode.HALF_DOWN);
    }

    public LocalDate getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
    }

    public void setX(LocalDate x) {
        this.x = x;
    }

    public void setY(BigDecimal y) {
        this.y = y;
    }

    @Override
    public String toString() {

        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Point)) return false;
        Point point = (Point) o;
        return Objects.equals(getX(), point.getX()) && Objects.equals(getY(), point.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }

}

