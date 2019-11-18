public class PtsFunc extends AbsFunc {
    private Point[] pts = {};
    int size;

    public PtsFunc(double[] xs, double[] ys){
        if(xs.length != ys.length){throw new RuntimeException("Different sizes of array");}
        size = xs.length;
        for(int i = 0; i < size; i++){
            addPoint(xs[i], ys[i]);
        }
    }

    Point getPoint(int i){
        if(i < 0 || i >= size){throw new RuntimeException("Wrong index!!!");}
        return pts[i];
    }

    @Override
    double solve(double x){
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

    @Override
    void readFromFile(String filename){

    }

    public void addPoint(double x, double y) {
        // Створюємо масив, більший на один елемент:
        Point[] p1 = new Point[pts.length + 1];
        // Копіюємо всі елементи:
        System.arraycopy(pts, 0, p1, 0, pts.length);
        // Записуємо нову точку в останній елемент:
        p1[pts.length] = new Point(x, y);
        pts = p1; // Тепер p вказує на новий масив
    }
}
