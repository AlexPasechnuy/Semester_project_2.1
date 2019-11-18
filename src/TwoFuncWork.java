import java.util.Arrays;

public class TwoFuncWork {
    AbsFunc f,g;
    double eps;

    TwoFuncWork(AbsFunc f, AbsFunc g){
        this.f = f;
        this.g = g;
    }

    public double dichotomy(double from, double to){
        double eps = this.eps/10000;
        double x = 0;
        while(Math.abs(from - to)>eps){
            double c = (from + to)/2;
            if(f.solve(from) * g.solve(c)<=0)   to = c;
            else    from = c;
            x = (from + to)/2;
        }
        return x;
    }

    Point[] findInters(double from, double to){
        eps = (to - from)/1000;
        Point[] inters = new Point[0];
        //dichotomy method for finding intersections
//        for(; from < to; from+= eps){
//            if(Math.abs(f.solve(from) - g.solve(from))< 0.5){
//                inters = Arrays.copyOf(inters, inters.length + 1);
//                double dich = dichotomy(from, from + eps);
//                inters[inters.length - 1] = new Point(dich, g.solve(dich));
//            }
//        }
        for(;from < to; from += eps){
            double x = dichotomy(from, from+eps);
            if(Math.abs(f.solve(x) - g.solve(x)) < eps*20){
                inters = Arrays.copyOf(inters, inters.length + 1);
                inters[inters.length - 1] = new Point(x, g.solve(x));
            }
        }
        return inters;
    }
}
