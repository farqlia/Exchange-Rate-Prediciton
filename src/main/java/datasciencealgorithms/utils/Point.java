package datasciencealgorithms.utils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public class Point<K> {

    private final K x;     // aka key
    private final BigDecimal y;     // aka value

    public Point(K x, BigDecimal y) {
        this.x = x;
        this.y = y.setScale(10, RoundingMode.HALF_DOWN);
    }

    public K getX() {
        return x;
    }

    public BigDecimal getY() {
        return y;
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
        Point<?> point = (Point<?>) o;
        return Objects.equals(getX(), point.getX()) && Objects.equals(getY(), point.getY());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getX(), getY());
    }
}
