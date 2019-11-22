package InterpolMethods;

import Functions.Point;

public class LangrangePolyn implements Interpol{
    public double solve(double x, Point[] pts){
        double lagrangePol = 0;

        for (int i = 0; i < pts.length; i++)
        {
            double basicsPol = 1;
            for (int j = 0; j < pts.length; j++)
            {
                if (j != i)
                {
                    basicsPol *= (x - pts[j].getX())/(pts[i].getX() - pts[j].getX());
                }
            }
            lagrangePol += basicsPol * pts[i].getY();
        }

        return lagrangePol;
    }
}
