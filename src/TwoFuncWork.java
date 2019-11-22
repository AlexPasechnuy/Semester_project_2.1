import FindIntersMethods.Dichotomy;
import FindIntersMethods.FindInters;
import Functions.AbsFunc;
import Functions.Point;
import Functions.PolynFunc;
import Functions.PtsFunc;
import InterpolMethods.LangrangePolyn;
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
    FindInters findInters = new Dichotomy();

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

    TwoFuncWork(AbsFunc f, AbsFunc g, FindInters findInters){
        this.f = f;
        this.g = g;
        this.findInters = findInters;
    }

    TwoFuncWork(){  }


    Point[] findInters(double from, double to){
        from = Math.max(getFrom(), from);
        to = Math.min(getTo(), to);
        eps = (to - from)/100;
        Point[] inters = new Point[0];
        //dichotomy method for finding intersections
        for(;from < to; from += eps){
            double x = findInters.solve(from, from+eps, f, g);
            if(Math.abs(f.solve(x) - g.solve(x)) < eps*3){
                inters = Arrays.copyOf(inters, inters.length + 1);
                inters[inters.length - 1] = new Point(x, g.solve(x));
                while(Math.abs(f.solve(x) - g.solve(x)) < eps*3){
                    x = findInters.solve(from, from+eps, f, g);
                    from += eps;
                }
            }
        }
        return inters;
    }


    Point[] findInters(){
        return findInters(getFrom(), getTo());
    }

    private double[] stringToArr(String str){
        double[] res = new double[0];
        StringTokenizer stx = new StringTokenizer(str);
        while(stx.hasMoreTokens()){
            res = Arrays.copyOf(res, res.length + 1);
            res[res.length - 1] = Double.parseDouble(stx.nextToken());
        }
        return res;
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

            f = new PtsFunc(stringToArr(xs),stringToArr(ys), new LangrangePolyn());
            g = new PolynFunc(stringToArr(coefs));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
