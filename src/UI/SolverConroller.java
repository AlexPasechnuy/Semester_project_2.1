package UI;

import Functions.AbsFunc;
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
import javafx.util.converter.DoubleStringConverter;

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
    AbsFunc g = new PolynFunc();
    TwoFuncWork fw = new TwoFuncWork();
    ObservableList<PtsRow> firstFuncPts;

    @FXML private TableView pointsTable;
    @FXML private  TableColumn<PtsRow, Double> xPts;
    @FXML private TableColumn<PtsRow, Double> yPts;
    @FXML private Button addPtBtn;
    @FXML private TextField secondFunc;
    @FXML private Button Solve;
    @FXML private TableView resTable;
    @FXML private TableColumn<PtsRow, Double> xRes;
    @FXML private TableColumn<PtsRow, Double> yRes;
    @FXML private BorderPane graphPane;

    @Override
    public void initialize(URL location, ResourceBundle resources){
        ptsTableInit();
    }

    private void ptsTableInit(){
        firstFuncPts = FXCollections.observableArrayList();
        for(int i = 0; i < f.getPtsNum(); i++){
            firstFuncPts.add(new PtsRow(f.getPoint(i).getX(),f.getPoint(i).getY()));
        }
        pointsTable.setItems(firstFuncPts);
        xPts.setCellValueFactory(new PropertyValueFactory<>("x"));
        xPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        xPts.setOnEditCommit(t->updateX(t));
        yPts.setCellValueFactory(new PropertyValueFactory<>("y"));
        yPts.setCellFactory(TextFieldTableCell.forTableColumn(new DoubleStringConverter()));
        yPts.setOnEditCommit(t->updateY(t));
    }

    private void resTableInit(){

    }

    private void updateX(TableColumn.CellEditEvent<PtsRow, Double> t) {
        PtsRow temp = t.getTableView().getItems().get(t.getTablePosition().getRow());
        System.out.println(t.getTablePosition().getRow());
        temp.setX(t.getNewValue());
        firstFuncPts.get(t.getTablePosition().getRow()).setX(t.getNewValue());
        System.out.println(t.getNewValue());
    }

    private void updateY(TableColumn.CellEditEvent<PtsRow, Double> t) {
        firstFuncPts.get(t.getTablePosition().getRow()).setY(t.getNewValue());
        System.out.println(t.getNewValue());
    }
}
