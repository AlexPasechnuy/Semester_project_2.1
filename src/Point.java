import javax.jws.Oneway;

public class Point {
    double x, y;

    Point(double x, double y){
        this.x = x;
        this.y = y;
    }

    Point(){
        this.x = 0;
        this.y = 0;
    }

    public double getX(){return x;}

    public double getY(){return y;}

    public void setX(double x){this.x = x;}

    public void setY(double y){this.y = y;}

    public void setPoint(double x, double y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString(){
        return "(" + (double)Math.round(x * 1000)/ 1000 + "; " + (double)Math.round(y * 1000)/ 1000 + ")";
    }
}
