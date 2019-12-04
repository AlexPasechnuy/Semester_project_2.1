package Functions;

public class PolynFunc extends AbsFunc {
    int n;
    double[] coefs;

    public PolynFunc(double[] coefs) {
        n = coefs.length - 1;
        this.coefs = coefs;
    }

    public double solve(double x) {
        double res = coefs[n];
        for (int i = 0; i < n; i++) {
            res += coefs[i] * Math.pow(x, n - i);
        }
        return res;
    }

    public double getFuncPower(){return n;}

    public double getCoef(int i){
        if (i < 0 || i > n){throw new RuntimeException("Wrong index");}
        return coefs[i];
    }
}