package EqSolve.PolynomEq;

public class PolynEq extends AbsPolynEq {
    int n;
    double[] coefs;

    PolynEq(double[] coefs) {
        n = coefs.length - 1;
        this.coefs = coefs;
    }

    double solve(double x) {
        double res = coefs[n];
        for (int i = 0; i < n; i++) {
            res += coefs[i] * Math.pow(x, n - i);
        }
        return res;
    }

    void readFromFile(String filename) {

    }

    double getN(){return n;}

    double getCoef(int i){
        if (i < 0 || i > n){throw new RuntimeException("Wrong index");}
        return coefs[i];
    }
}