package UI;

import Exceptions.CalculateBoundsException;
import Exceptions.WrongFunctionFormatException;
import FindIntersMethods.Dichotomy;
import Functions.AbsFunc;
import Functions.Point;
import Functions.PolynFunc;
import Functions.PtsFunc;
import TwoFuncWork.TwoFuncWork;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.BorderPane;
import javafx.stage.FileChooser;
import javafx.util.converter.DoubleStringConverter;

import javax.xml.bind.JAXBException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
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

    @FXML private TableView pointsTable;
    @FXML private  TableColumn<PtsRow, Double> xPts, yPts;
    @FXML private Button addPtBtn, Solve;
    @FXML private TextField secondFunc;
    @FXML private TableView resTable;
    @FXML private TableColumn<PtsRow, Double> xRes, yRes;
    @FXML private BorderPane graphPane;
    @FXML private TextField fromText, toText;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ptsTableInit();
    }

    private void ptsTableInit(){
        firstFuncPts = FXCollections.observableArrayList();
        for(int i = 0; i < f.getPtsNum(); i++) {
            firstFuncPts.add(new PtsRow(f.getPoint(i).getX(), f.getPoint(i).getY()));
        }
        pointsTable.setItems(firstFuncPts);
        xPts.setCellValueFactory(new PropertyValueFactory<>("x"));
        xPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        xPts.setOnEditCommit(t->updateX(t));
        yPts.setCellValueFactory(new PropertyValueFactory<>("y"));
        yPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yPts.setOnEditCommit(t->updateY(t));
    }

    private void resTableInit(Point[] results){
        ObservableList<PtsRow> res= FXCollections.observableArrayList();
        for(int i = 0; i < results.length; i++) {
            res.add(new PtsRow((double)Math.round(results[i].getX() * 1000)/ 1000, (double)Math.round(results[i].getY() * 1000)/ 1000));
        }
        resTable.setItems(res);
        xRes.setCellValueFactory(new PropertyValueFactory<>("x"));
        yRes.setCellValueFactory(new PropertyValueFactory<>("y"));
    }

    private void updateX(TableColumn.CellEditEvent<PtsRow, Double> t) {
        firstFuncPts.get(t.getTablePosition().getRow()).setX(t.getNewValue());
    }

    private void updateY(TableColumn.CellEditEvent<PtsRow, Double> t) {
        firstFuncPts.get(t.getTablePosition().getRow()).setY(t.getNewValue());
    }

    private void functionsInit(){   //initializes functions by data in
        f = new PtsFunc();
        g = new PolynFunc(secondFunc.getText());
        for (PtsRow row : firstFuncPts) {
            f.addPoint(row.getX(), row.getY());
        }
        fw = new TwoFuncWork(f,g,new Dichotomy());
    }

    @FXML
    private void solveClick(javafx.event.ActionEvent event) {
        try {
            functionsInit();
            double from, to;
            if(fromText.getText().isEmpty()) {
                from = fw.getFrom();
            }else{
                from = Double.parseDouble(fromText.getText());
            }
            if(toText.getText().isEmpty()){
                to = fw.getTo();
            }else{
                to = Double.parseDouble(toText.getText());
            }
            Point[] interPts = fw.findInters(from,to);
            resTableInit(interPts);
        }catch(WrongFunctionFormatException ex){
            showError("Wrong format of function");
        }catch(NumberFormatException ex){
            showError("FROM or TO is not a number");
        }catch(CalculateBoundsException ex){
            showError("User's boundaries have no intersection with function boundaries");
        }
    }

    @FXML
    private void addPtClick(javafx.event.ActionEvent event) {
        f.addPoint(0,0);
        ptsTableInit();
    }

    @FXML
    private void saveClick(javafx.event.ActionEvent event) {
        functionsInit();
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

    @FXML
    private void doOpen(javafx.event.ActionEvent event) {
        FileChooser fileChooser = getFileChooser("Open XML file");
        File file;
        if ((file = fileChooser.showOpenDialog(null)) != null) {
            try {
                fw.readFromFile(file.getCanonicalPath());
                f = (PtsFunc)fw.getF();
                g = (PolynFunc) fw.getG();
                secondFunc.setText(g.getFunc());
                ptsTableInit();
            } catch (IOException e) {
                showError("No such file");
            }
        }
    }
}
