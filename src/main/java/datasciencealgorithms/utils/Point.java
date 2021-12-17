package datasciencealgorithms.utils;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class Point<K> {

    private K x;     // aka key
    private BigDecimal y;     // aka value

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
}
