public class Main {
    public static void main(String[] args) {
        double[] coefs = {1,1,-1,-1};
        PolynFunc f = new PolynFunc(coefs);
        double[] xs = {-5,0,7};
        double[] ys = {16,1,64};
        PtsFunc g = new PtsFunc(xs,ys);
        System.out.println(g.solve(-1));
        System.out.println(g.solve(2));
        TwoFuncWork inter = new TwoFuncWork(f,g);
        Point[] interPoints = inter.findInters(g.getMinX(),g.getMaxX());
        for(int i = 0; i < interPoints.length; i++){
            System.out.println(interPoints[i].toString());
        }
    }
}
