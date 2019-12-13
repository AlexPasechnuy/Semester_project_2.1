package FindIntersMethods;

import Exceptions.WrongFunctionFormatException;
import Functions.AbsFunc;

public interface FindInters {
    public double solve(double from, double to, AbsFunc f, AbsFunc g) throws WrongFunctionFormatException;
}
