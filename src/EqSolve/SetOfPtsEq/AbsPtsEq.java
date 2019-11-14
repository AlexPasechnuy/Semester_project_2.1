package EqSolve.SetOfPtsEq;

import EqSolve.AbsEq;
import EqSolve.Point;

public abstract class AbsPtsEq extends AbsEq {
    abstract Point getPoint(int i);
    abstract double solve(double x);
    abstract void readFromFile(String filename);
    abstract double interpol(double x);
}
