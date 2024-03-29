package TwoFuncWork;

import Exceptions.CalculateBoundsException;
import Exceptions.SameFuncsException;
import Exceptions.WrongFunctionFormatException;
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
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.StringTokenizer;

public class TwoFuncWork {
    AbsFunc f,g;
    double eps;
    FindInters findInters = new Dichotomy();
    double minY;
    double maxY;

    private void checkBoundY(double arg){
        if(arg < minY){minY=arg;}
        else if(arg > maxY){maxY=arg;}
    }

    public double getMinY(){return minY;}

    public double getMaxY(){return maxY;}

    public AbsFunc getF(){return f;}

    public AbsFunc getG(){return g;}

    public double getFrom(){
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

    public double getTo(){
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

    public TwoFuncWork(AbsFunc f, AbsFunc g, FindInters findInters){
        this.f = f;
        this.g = g;
        this.findInters = findInters;
    }

    public TwoFuncWork(){  }


    public Point[] findInters(double from, double to) throws WrongFunctionFormatException, CalculateBoundsException, SameFuncsException {
        if(from > getTo() || to < getFrom() ){throw new CalculateBoundsException();
        }
        double dif;
        from = Math.max(getFrom(), from);
        to = Math.min(getTo(), to);
        double fRes, gRes;
        minY = maxY = f.solve(from);
        eps = (to - from)/100;
        Point[] inters = new Point[0];
        //dichotomy method for finding intersections
        for(double i = from;i < to; i += eps){
            double x = findInters.solve(i, i+eps, f, g);
            fRes = f.solve(x);
            gRes = g.solve(x);
            checkBoundY(fRes);
            checkBoundY(gRes);
            if((dif = Math.abs(fRes - gRes)) < eps*3){
                inters = Arrays.copyOf(inters, inters.length + 1);
                inters[inters.length - 1] = new Point(x, gRes);
                double beforeLoop = i;
                while(Math.abs(fRes - gRes) < eps*3){
                    checkBoundY(fRes);
                    checkBoundY(gRes);
                    x = findInters.solve(i, i+eps, f, g);
                    fRes = f.solve(x);
                    gRes = g.solve(x);
                    if(Math.abs(fRes - gRes) < dif){
                        inters[inters.length - 1] = new Point(x, gRes);
                    }
                    i += eps;
                    if(i >= to){
                        if(beforeLoop == from){
                            throw new SameFuncsException();
                        }
                        break;
                    }
                }
            }
        }
        return inters;
    }


    public Point[] findInters() throws WrongFunctionFormatException, CalculateBoundsException, SameFuncsException{
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
            String func = elem.getElementsByTagName("func").item(0).getTextContent();

            f = new PtsFunc(stringToArr(xs),stringToArr(ys), new LangrangePolyn());
            g = new PolynFunc(func);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void writeToFile(String filename){
            try {
                PtsFunc ptsWrite = (PtsFunc) f;
                PolynFunc polynWrite = (PolynFunc) g;
                DocumentBuilderFactory dbf;
                DocumentBuilder db;
                Document doc;

                dbf = DocumentBuilderFactory.newInstance();
                db = dbf.newDocumentBuilder();
                doc = db.newDocument();

                Element twoFuncts = doc.createElement("twoEqsWork");

                Element ptsEq = doc.createElement("PtsEq");
                Element polynEq = doc.createElement("PolynEq");
                //filling ptseEq
                String xString = "";
                String yString = "";
                for(int  i = 0; i < ptsWrite.getPtsNum(); i++){
                    xString += ptsWrite.getPoint(i).getX() + " ";
                    yString += ptsWrite.getPoint(i).getY() + " ";
                }
                Element xs = doc.createElement("xs");
                xs.setTextContent(xString);
                Element ys = doc.createElement("ys");
                ys.setTextContent(yString);
                ptsEq.appendChild(xs);
                ptsEq.appendChild(ys);
                Element coefs = doc.createElement("func");
                coefs.setTextContent(polynWrite.getFunc());
                polynEq.appendChild(coefs);

                twoFuncts.appendChild(ptsEq);
                twoFuncts.appendChild(polynEq);
                doc.appendChild(twoFuncts);

                Transformer transformer = TransformerFactory.newInstance().newTransformer();
                transformer.transform(new DOMSource(doc),
                        new StreamResult(new FileOutputStream(new File(filename))));
            }catch (Exception ex){
                ex.printStackTrace();
            }
    }
}
