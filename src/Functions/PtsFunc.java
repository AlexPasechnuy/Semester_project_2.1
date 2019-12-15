package Functions;

import InterpolMethods.Interpol;

public class PtsFunc extends AbsFunc {
    private Point[] pts = {};
    int size;
    Interpol inter;

    public int getPtsNum(){return pts.length;}

    public PtsFunc(double[] xs, double[] ys, Interpol inter){
        if(xs.length != ys.length){throw new RuntimeException("Different sizes of array");}
        size = xs.length;
        for(int i = 0; i < size; i++){
            addPoint(xs[i], ys[i]);
        }
        this.inter = inter;
    }

    public PtsFunc(){    }

    public Point getPoint(int i){
        if(i < 0 || i >= size){throw new RuntimeException("Wrong index!!!");}
        return pts[i];
    }

    @Override
    public double solve(double x){
        return inter.solve(x, pts);
    }


    public void addPoint(double x, double y) {
        Point[] p1 = new Point[pts.length + 1];
        System.arraycopy(pts, 0, p1, 0, pts.length);
        p1[pts.length] = new Point(x, y);
        pts = p1;
    }

    public double getMinX(){
        double min = pts[0].x;
        for(int i = 1; i < size; i++){
            if(pts[i].x < min){
                min = pts[i].x;
            }
        }
        return min;
    }

    public double getMaxX(){
        double max = pts[0].x;
        for(int i = 1; i < size; i++){
            if(pts[i].x > max){
                max = pts[i].x;
            }
        }
        return max;
    }

    public void setPts(Point[] pts){this.pts = pts;}

    public void setPoint(int i, double x, double y){
        if(i < 0 || i >= size){throw new RuntimeException("Wrong index!!!");}
        pts[i]= new Point(x,y);
    }
}
