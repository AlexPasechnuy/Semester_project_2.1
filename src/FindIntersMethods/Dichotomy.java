package FindIntersMethods;

import Exceptions.WrongFunctionFormatException;
import Functions.AbsFunc;

public class Dichotomy implements FindInters {
    public double solve(double from, double to, AbsFunc f, AbsFunc g) throws WrongFunctionFormatException {
        double eps = (to - from)/10000;
        double x = 0;
        while(Math.abs(from - to)>eps){
            double c = (from + to)/2;
            if(f.solve(from) * g.solve(c)<=0)   to = c;
            else    from = c;
            x = (from + to)/2;
        }
        return x;
    }
}
