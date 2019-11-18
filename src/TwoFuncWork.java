import java.util.Arrays;

public class TwoFuncWork {
    AbsFunc f,g;

    TwoFuncWork(AbsFunc f, AbsFunc g){
        this.f = f;
        this.g = g;
    }

    public double dichotomy(double from, double to){
        double eps = 0.00001d;
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
        Point[] inters = new Point[0];
        //dichotomy method for finding intersections
        for(; from < to; from+= 0.05){

        }
        inters = Arrays.copyOf(inters, inters.length + 1);
        double dich = dichotomy(from, to);
        inters[inters.length - 1] = new Point(dich, f.solve(dich));
        return inters;
    }
}
