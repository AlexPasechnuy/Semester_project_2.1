package Functions;

import Exceptions.WrongFunctionFormatException;

public abstract class AbsFunc {
    public abstract double solve(double x)throws WrongFunctionFormatException;
}
