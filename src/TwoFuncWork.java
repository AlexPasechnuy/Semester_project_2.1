import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.File;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TwoFuncWork {
    AbsFunc f,g;
    double eps;

    private double getFrom(){
        if (f instanceof PtsFunc){
            return ((PtsFunc) f).getMinX();
        }
        else if(g instanceof PtsFunc){
            return ((PtsFunc) g).getMinX();
        }
        else{
            throw new RuntimeException("There is no PtsFunc class");
        }
    }

    private double getTo(){
        if (f instanceof PtsFunc){
            return ((PtsFunc) f).getMaxX();
        }
        else if(g instanceof PtsFunc){
            return ((PtsFunc) g).getMaxX();
        }
        else{
            throw new RuntimeException("There is no PtsFunc class");
        }
    }

    TwoFuncWork(AbsFunc f, AbsFunc g){
        this.f = f;
        this.g = g;
    }

    TwoFuncWork(){  }

    public double dichotomy(double from, double to){
        double eps = this.eps/10000;
        double x = 0;
        while(Math.abs(from - to)>eps){
            double c = (from + to)/2;
            if(f.solve(from) * g.solve(c)<=0)   to = c;
            else    from = c;
            x = (from + to)/2;
        }
        return x;
    }

    Point[] findInters(){
        double from = getFrom();
        double to = getTo();
        eps = (to - from)/10000;
        Point[] inters = new Point[0];
        //dichotomy method for finding intersections
        for(;from < to; from += eps){
            double x = dichotomy(from, from+eps);
            if(Math.abs(f.solve(x) - g.solve(x)) < eps*3){
                inters = Arrays.copyOf(inters, inters.length + 1);
                inters[inters.length - 1] = new Point(x, g.solve(x));
                while(Math.abs(f.solve(x) - g.solve(x)) < eps*3){
                    x = dichotomy(from, from+eps);
                    from += eps;
                }
            }
        }
        return inters;
    }

    public void readFromFile(String filename){
        try {

            File fXmlFile = new File(filename);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(fXmlFile);
            NodeList nList = doc.getElementsByTagName("PtsEq");

            Node nNode = nList.item(0);
            Element elem = (Element) nNode;
            String xs = elem.getElementsByTagName("xs").item(0).getTextContent();
            String ys = elem.getElementsByTagName("ys").item(0).getTextContent();


            nList = doc.getElementsByTagName("PolynEq");

            nNode = nList.item(0);
            elem = (Element) nNode;
            String coefs = elem.getElementsByTagName("coefs").item(0).getTextContent();

            double[] x = new double[0];
            double[] y = new double[0];
            double[] cs = new double[0];

            StringTokenizer stx = new StringTokenizer(xs);
            while(stx.hasMoreTokens()){
                x = Arrays.copyOf(x, x.length + 1);
                x[x.length - 1] = Double.parseDouble(stx.nextToken());
            }

            stx = new StringTokenizer(ys);
            while(stx.hasMoreTokens()){
                y = Arrays.copyOf(y, y.length + 1);
                y[y.length - 1] = Double.parseDouble(stx.nextToken());
            }

            stx = new StringTokenizer(coefs);
            while(stx.hasMoreTokens()){
                cs = Arrays.copyOf(cs, cs.length + 1);
                cs[cs.length - 1] = Double.parseDouble(stx.nextToken());
            }

            f = new PtsFunc(x,y);
            g = new PolynFunc(cs);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
