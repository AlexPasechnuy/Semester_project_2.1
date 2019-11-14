package EqSolve.PolynomEq;

import EqSolve.AbsEq;

public abstract class AbsPolynEq extends AbsEq{
    abstract double solve(double x);

    abstract void readFromFile(String filename);

    abstract double getN();

    abstract double getCoef(int i);
}
