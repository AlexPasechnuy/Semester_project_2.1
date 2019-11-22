package InterpolMethods;

import Functions.Point;

public interface Interpol {
    public abstract double solve(double x, Point[] pts);
}
