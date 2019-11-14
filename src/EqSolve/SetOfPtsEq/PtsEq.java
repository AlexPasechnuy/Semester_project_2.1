package EqSolve.SetOfPtsEq;

import EqSolve.Point;

public class PtsEq extends AbsPtsEq {
    Point[] pts;
    int size;

    PtsEq(double[] xs, double[] ys){
        if(xs.length != ys.length){throw new RuntimeException("Different sizes of array");}
        size = xs.length;
        pts = new Point[size];
        for(int i = 0; i < size; i++){
            pts[i].setX(xs[i]);
            pts[i].setY(ys[i]);
        }
    }

    Point getPoint(int i){
        if(i < 0 || i >= size){throw new RuntimeException("Wrong index!!!");}
        return pts[i];
    }

    double solve(double x){
        for(int i = 0; i < pts.length; i++){
            if(pts[i].getX() == x){return pts[i].getY();}
        }
        return interpol(x);
    }

    void readFromFile(String filename){

    }

    double interpol(double x){
        double lagrangePol = 0;

        for (int i = 0; i < size; i++)
        {
            double basicsPol = 1;
            for (int j = 0; j < size; j++)
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
