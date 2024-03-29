package UI;

import Exceptions.CalculateBoundsException;
import Exceptions.SameFuncsException;
import Exceptions.WrongFunctionFormatException;
import FindIntersMethods.Dichotomy;
import Functions.Point;
import Functions.PolynFunc;
import Functions.PtsFunc;
import TwoFuncWork.TwoFuncWork;
import javafx.application.Platform;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.SnapshotParameters;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.RenderedImage;
import java.io.*;
import java.net.URL;
import java.util.Base64;
import java.util.ResourceBundle;

public class SolverConroller implements Initializable {
    public class PtsRow{
        private SimpleDoubleProperty x,y;
        public PtsRow(double x, double y){
            this.x = new SimpleDoubleProperty(x);
            this.y = new SimpleDoubleProperty(y);
        }

        public double getX(){return x.get();}
        public double getY(){return y.get();}
        public void setX(double x){this.x.set(x);}
        public void setY(double y){this.y.set(y);}
    }

    PtsFunc f = new PtsFunc();
    PolynFunc g = new PolynFunc();
    TwoFuncWork fw = new TwoFuncWork();
    ObservableList<PtsRow> firstFuncPts;
    ObservableList<PtsRow> resPts;
    double xFrom, xTo;

    @FXML private TableView<PtsRow> ptsTable;
    @FXML private  TableColumn<PtsRow, Double> xPts, yPts;
    @FXML private Button addPtBtn, Solve;
    @FXML private TextField secondFunc;
    @FXML private TableView resTable;
    @FXML private TableColumn<PtsRow, Double> xRes, yRes;
    @FXML private BorderPane graphPane;
    @FXML private TextField fromText, toText;
    @FXML private TextField xAdd, yAdd;
    @FXML private TextField rootsNum;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ActionEvent event = new ActionEvent();
        newClick(event);
    }

    private void ptsTableInit(){
        ptsTable.setItems(firstFuncPts);
        xPts.setCellValueFactory(new PropertyValueFactory<>("x"));
        xPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        xPts.setOnEditCommit(t->updateX(t));
        yPts.setCellValueFactory(new PropertyValueFactory<>("y"));
        yPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yPts.setOnEditCommit(t->updateY(t));
    }

    private void resTableInit(Point[] results){
        resPts= FXCollections.observableArrayList();
        for(int i = 0; i < results.length; i++) {
            resPts.add(new PtsRow((double)Math.round(results[i].getX() * 1000)/ 1000, (double)Math.round(results[i].getY() * 1000)/ 1000));
        }
        resTable.setItems(resPts);
        xRes.setCellValueFactory(new PropertyValueFactory<>("x"));
        yRes.setCellValueFactory(new PropertyValueFactory<>("y"));
    }

    private void updateX(TableColumn.CellEditEvent<PtsRow, Double> t) {
        firstFuncPts.get(t.getTablePosition().getRow()).setX(t.getNewValue());
    }

    private void updateY(TableColumn.CellEditEvent<PtsRow, Double> t) {
        firstFuncPts.get(t.getTablePosition().getRow()).setY(t.getNewValue());
    }

    private void InitFunctions(){   //initializes functions by data in UI windows
        f = new PtsFunc();
        g = new PolynFunc(secondFunc.getText());
        for (PtsRow row : firstFuncPts) {
            f.addPoint(row.getX(), row.getY());
        }
        fw = new TwoFuncWork(f,g,new Dichotomy());
    }

    private void initUI(){
        f = (PtsFunc)fw.getF();
        g = (PolynFunc) fw.getG();
        secondFunc.setText(g.getFunc());
        firstFuncPts = FXCollections.observableArrayList();
        for(int i = 0; i < f.getPtsNum(); i++) {
            firstFuncPts.add(new PtsRow(f.getPoint(i).getX(), f.getPoint(i).getY()));
        }
        ptsTableInit();
    }

    @FXML
    private void solveClick(javafx.event.ActionEvent event) {
        try {
            if(firstFuncPts.size() == 0 || secondFunc.getText().isEmpty()){
                throw new NullPointerException();
            }
            InitFunctions();
            if (fromText.getText().isEmpty()) {
                xFrom = fw.getFrom();
            } else {
                xFrom = Math.max(Double.parseDouble(fromText.getText()), fw.getFrom());
            }
            if (toText.getText().isEmpty()) {
                xTo = fw.getTo();
            } else {
                xTo = Math.min(Double.parseDouble(toText.getText()),fw.getTo());
            }
            Point[] interPts = fw.findInters(xFrom,xTo);
            if(interPts.length == 0){
                rootsNum.setText("No roots");
            }
            else{
                rootsNum.setText(interPts.length + " roots");
            }
            resTableInit(interPts);
            constructGraphs();
        }catch(WrongFunctionFormatException ex){
            showError("Wrong format of function");
        }catch(NumberFormatException ex){
            showError("FROM or TO is not a number");
        }catch(CalculateBoundsException ex){
            showError("User's boundaries have no intersection with function boundaries");
        }catch(SameFuncsException ex){
            rootsNum.setText(ex.getMessage());
            constructGraphs();
        }catch(NullPointerException ex) {
            showError("f(x) or g(x) are not defined");
        }
    }

    @FXML
    private void addPtClick(javafx.event.ActionEvent event) {
        double x = 0;
        double y = 0;
        try {
            if (!xAdd.getText().isEmpty()) {
                x = Double.parseDouble(xAdd.getText());
            }
            if (!yAdd.getText().isEmpty()) {
                y = Double.parseDouble(yAdd.getText());
            }
            firstFuncPts.add(new PtsRow(x,y));
            ptsTableInit();
        }catch(NumberFormatException ex){
            showError("X or Y are not numbers!");
        }
    }

    @FXML
    private void delPtClick(javafx.event.ActionEvent event) {
        try {
            int pos = ptsTable.getSelectionModel().getSelectedIndex();
            if (pos == -1) {
                throw new NullPointerException();
            }
            firstFuncPts.remove(pos);
            ptsTableInit();
        }catch(NullPointerException ex){
            showError("Please, choose point to delete!");
        }
    }

    @FXML
    private void saveClick(javafx.event.ActionEvent event) {
        InitFunctions();
        FileChooser fileChooser = getFileChooser("Save XML file");
        File file;
        if ((file = fileChooser.showSaveDialog(null)) != null) {
            try {
                fw.writeToFile(file.getCanonicalPath());
                showMessage("Results are saved");
            } catch (Exception e) {
                showError("Error write to file");
            }
        }
    }

    @FXML
    private void aboutClick(javafx.event.ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("About program");
        alert.setHeaderText("Program calculates intersections of two functions and constructs graphs\n" +
                "Written by student of 1.KN201.8g, Alex Pasechnuy\n" +
                "Email: \"AlexPasechnuy@gmail.com\"\n"+
                "Thank you for using my program❤❤❤");
        alert.showAndWait();
    }

    @FXML
    private void helpClick(javafx.event.ActionEvent event) {
        try {
            File file = new File("src\\HelpFiles\\Main.html");
            Desktop.getDesktop().open(file);
        }catch(IOException ex){
            showError("File not found");
        }
    }

    @FXML
    private void newClick(javafx.event.ActionEvent event) {
        f = new PtsFunc();
        g = new PolynFunc();
        fw = new TwoFuncWork(f,g, new Dichotomy());
        resTable.getItems().removeAll();
        Point[] nulpts = new Point[0];
        resTableInit(nulpts);
        fromText.clear();
        toText.clear();
        rootsNum.clear();
        NumberAxis xAxis = new NumberAxis(-5, 5, 1);
        NumberAxis yAxis = new NumberAxis(-5, 5, 1);
        xAxis.setLabel("x");
        yAxis.setLabel("y");
        graphPane.getChildren().clear();
        graphPane.setCenter(new LineChart<Number,Number>(xAxis,yAxis));
        initUI();
    }

    @FXML
    private void exitClick(javafx.event.ActionEvent event) {
        Platform.exit();
    }

    private void constructGraphs(){
        try{
            NumberAxis xAxis = new NumberAxis(xFrom,xTo,(xTo-xFrom)/20);
            NumberAxis yAxis = new NumberAxis(fw.getMinY(),fw.getMaxY(),(fw.getMaxY()- fw.getMinY())/20);
            xAxis.setLabel("x");
            yAxis.setLabel("y");
            LineChart<Number,Number> newChart = new LineChart<>(xAxis,yAxis);
            newChart.setCreateSymbols(false);
            graphPane.getChildren().clear();
            graphPane.getChildren().add(newChart);
            double step = (xTo-xFrom) / 100;
            XYChart.Series<Number,Number> fSeries = new XYChart.Series<>();
            fSeries.setName("f(x)");
            XYChart.Series<Number,Number> gSeries = new XYChart.Series<>();
            gSeries.setName("g(x)");
            for(double x = xFrom;x <= xTo;x+=step) {
                fSeries.getData().add(new XYChart.Data<>(x, f.solve(x)));
                gSeries.getData().add(new XYChart.Data<>(x, g.solve(x)));
            }
            fSeries.getData().add(new XYChart.Data<>(xTo,f.solve(xTo)));
            gSeries.getData().add(new XYChart.Data<>(xTo,g.solve(xTo)));
            newChart.getData().addAll(fSeries, gSeries);
            for(int i = 0; i < resPts.size(); i++){
                XYChart.Series<Number,Number> newSeries = new XYChart.Series<>();
                newSeries.getData().add(new XYChart.Data<>(resPts.get(i).getX(), resPts.get(i).getY()));
                newSeries.setName("Intersection" + (i+1));
                newChart.getData().add(newSeries);
            }
            graphPane.getChildren().clear();
            graphPane.setCenter(newChart);
        }catch (Exception ex){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Error");
            alert.showAndWait();
        }
    }

    private static void showError(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    private static void showMessage(String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("");
        alert.setHeaderText(message);
        alert.showAndWait();
    }

    public static FileChooser getFileChooser(String title) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
        fileChooser.setTitle(title);
        return fileChooser;
    }

    public String getImageBytes()
    {
        WritableImage image =  graphPane.snapshot(new SnapshotParameters(), null);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        RenderedImage img = SwingFXUtils.fromFXImage(image, null);
        try
        {
            ImageIO.write(img, "PNG", stream);
            byte[] buf = stream.toByteArray();
            stream.close();
            String image2 = Base64.getEncoder().encodeToString(buf);
            return image2;

        }
        catch (Exception e) { }

        return null;
    }
    public void saveHTML(File file, String encodedImage){
        try {
            FileWriter writer = new FileWriter(file);
            BufferedWriter bw = new BufferedWriter(writer);
            String content = ("<!DOCTYPE html>\n" +
                    "<html lang=\"en\">\n" +
                    "<head>\n" +
                    "    <meta charset=\"UTF-8\">\n" +
                    "    <title>Report</title>\n" +
                    "    <style>\n" +
                    "        td, th\n" +
                    "        {\n" +
                    "              border: 1px solid black;\n" +
                    "              border-collapse: collapse;\n" +
                    "              width: 50%;\n" +
                    "        }\n" +
                    "        table\n" +
                    "        {\n" +
                    "            width: 100%; text-align: center;\n" +
                    "        }\n" +
                    "        .container\n" +
                    "        {\n" +
                    "            margin: auto;\n" +
                    "            display: flex;\n" +
                    "            flex-wrap: wrap;\n" +
                    "        }\n" +
                    "        .table_wrapper\n" +
                    "        {\n" +
                    "            display: flex;\n" +
                    "            width: 100%;\n" +
                    "            flex-direction: row;\n" +
                    "        }\n" +
                    "        .screenshot\n" +
                    "        {\n" +
                    "            position: relative;\n" +
                    "            display: flex;\n" +
                    "            width: 100%;\n" +
                    "        }\n" +
                    "        .roots\n" +
                    "        {\n" +
                    "            margin-left: 10vh;\n" +
                    "        }\n" +
                    "    </style>\n" +
                    "</head>\n" +
                    "<body>\n" +
                    "      <div class=\"container\">\n" +
                    "       <div class=\"table_wrapper\">\n" +
                    "        <table>\n" +
                    "          <tr>\n" +
                    "              <th colspan = \"2\">f(x)</th>\n" +
                    "          </tr>\n" +
                    "          <tr>\n" +
                    "            <th>x</th>\n" +
                    "            <th>y</th>\n" +
                    "          </tr>\n");
            for(int i = 0; i < firstFuncPts.size();i++)
            {
                content +=("<tr>\n" +
                        "<td>" + firstFuncPts.get(i).getX() + "</td>\n" +
                        "<td>"+ firstFuncPts.get(i).getY() +"</td>\n" +
                        "</tr>");
            }
            content+= "<br>" + "</br>";
            content+=(
                    " </table>\n" +
                            "          <table>\n" +
                            "          <tr>\n" +
                            "              <th colspan = \"2\">g(x)</th>\n" +
                            "          </tr>\n");
            content+=("<tr>\n" + "<td>" + g.getFunc() + "</tr>");
            content+=(
                    " </table>\n" +
                            "          <table>\n" +
                            "          <tr>\n" +
                            "              <th colspan = \"2\">Roots</th>\n" +
                            "          </tr>\n" +
                            "          <tr>\n" +
                            "            <th>x</th>\n" +
                            "            <th>y</th>\n" +
                            "          </tr>\n");
            ObservableList<PtsRow> res = resTable.getItems();
            for(int i = 0; i < res.size();i++)
            {
                content +=("<tr>\n" +
                        "<td>" + res.get(i).getX() + "</td>\n" +
                        "<td>"+ res.get(i).getY() +"</td>\n" +
                        "</tr>");
            }
            content += "<br /><br />";
            content+=(
                    "          </table>\n" +
                            "        </div>\n" +
                            "        <div class=\"screenshot\">\n" +
                            "            <img src=\"data:image/png;base64, ");
            content+= encodedImage + "\">\n";
            content+=("            </table>\n" +
                    "        </div>\n" +
                    "        </div>\n" +
                    "</body>\n" +
                    "</html>");
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void saveReportClick(javafx.event.ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setInitialDirectory(new File("."));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("HTML files (*.html)", "*.html"));
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("All files (*.*)", "*.*"));
        fileChooser.setTitle("Save report");
        File file;
        if ((file = fileChooser.showSaveDialog(null)) != null) {
            try {
                saveHTML(file, getImageBytes());
                showMessage("Report is saved");
            } catch (Exception e) {
                showError("Error saving report");
            }
        }
    }

    @FXML
    private void doOpen(javafx.event.ActionEvent event) {
        FileChooser fileChooser = getFileChooser("Open XML file");
        File file;
        if ((file = fileChooser.showOpenDialog(null)) != null) {
            try {
                newClick(event);
                fw.readFromFile(file.getCanonicalPath());
                initUI();
            } catch (IOException e) {
                showError("No such file");
            }
        }
    }
}