import Functions.Point;

public class Main {
    public static void main(String[] args) {
//        double[] coefs = {1,1,-1,-1};
//        PolynFunc f = new PolynFunc(coefs);
//        double[] xs = {-5,0,7};
//        double[] ys = {16,1,64};
//        PtsFunc g = new PtsFunc(xs,ys);
//        TwoFuncWork inter = new TwoFuncWork(f,g);
//        Point[] interPoints = inter.findInters();
//        for(int i = 0; i < interPoints.length; i++){
//            System.out.println(interPoints[i].toString());
//        }

        ////////////////////////////////////////////////////////////////////////////////////////////////

//        double[] coefs = {1,3,3,1};
//        PolynFunc f = new PolynFunc(coefs);
//        double[] xs = {-2,0,4,5};
//        double[] ys = {-3,3,15,18};
//        PtsFunc g = new PtsFunc(xs,ys);
//        TwoFuncWork inter = new TwoFuncWork(f,g);
//        Point[] interPoints1 = inter.findInters();
//        for(int i = 0; i < interPoints1.length; i++){
//            System.out.println(interPoints1[i].toString());
//        }
        //////////////////////////////////////////////////////////////////////////////////////////////////

//        double[] xs1 = {-3,1,0,4};
//        double[] ys1 = {30,2,6,2};
//        PtsFunc f = new PtsFunc(xs1,ys1);
//        double[] xs2 = {3,4,5,9};
//        double[] ys2 = {2,6,12,56};
//        PtsFunc g = new PtsFunc(xs2,ys2);
//        TwoFuncWork inter = new TwoFuncWork(f,g);
//        Point[] interPoints = inter.findInters();
//        for(int i = 0; i < interPoints.length; i++){
//            System.out.println(interPoints[i].toString());
//        }

        //////////////////////////////////////////////////////////////////////////////////////////////////
//        double[] coefs1 = {4,4,4};
//        PolynFunc f1 = new PolynFunc(coefs1);
//        double[] coefs2 = {3,3,0};
//        PolynFunc g1 = new PolynFunc(coefs2);
//        double[] xs1 = {-5,-4,-2};
//        double[] ys1 = {84,52,12};
//        PtsFunc f2 = new PtsFunc(xs1,ys1);
//        double[] xs2 = {-2,0,1,3};
//        double[] ys2 = {6,0,6,36};
//        PtsFunc g2 = new PtsFunc(xs2,ys2);
//        TwoFuncWork inter = new TwoFuncWork(f2,g2);
//        Point[] interPoints = inter.findInters(-3,9);
//        for(int i = 0; i < interPoints.length; i++){
//            System.out.println(interPoints[i].toString());
//        }
////////////////////////////////////////////////////////////////////////////////
        System.out.println("\nTwo roots: ");
        TwoFuncWork fw = new TwoFuncWork();
        fw.readFromFile("eqs1.xml");
        Point[] interPoints = fw.findInters();
        for(int i = 0; i < interPoints.length; i++){
            System.out.println(interPoints[i].toString());
        }

        System.out.println("\nTwo roots: ");
        fw.readFromFile("eqs2.xml");
        interPoints = fw.findInters();
        for(int i = 0; i < interPoints.length; i++){
            System.out.println(interPoints[i].toString());
        }

        System.out.println("\nNo roots: ");
        fw.readFromFile("eqs3.xml");
        interPoints = fw.findInters();
        for(int i = 0; i < interPoints.length; i++){
            System.out.println(interPoints[i].toString());
        }

        System.out.println("\nOne root: ");
        fw.readFromFile("eqs4.xml");
        interPoints = fw.findInters();
        for(int i = 0; i < interPoints.length; i++) {
            System.out.println(interPoints[i].toString());
        }
    }
}
