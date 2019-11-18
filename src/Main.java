public class Main {
    public static void main(String[] args) {
        double[] coefs = {2,0};
        PolynFunc f = new PolynFunc(coefs);
        double[] xs = {1,2,3,4,5};
        double[] ys = {2,4,7,8,9};
        PtsFunc g = new PtsFunc(xs,ys);
        TwoFuncWork inter = new TwoFuncWork(f,g);
        Point[] interPoints = inter.findInters(-100,100);
        System.out.println(interPoints[0].getX() + interPoints[0].getY());
    }
}
