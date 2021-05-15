package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.BarChart;
import sample.model.DataModel;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class BarChartSceneController implements Initializable {


    @FXML
    private BarChart mybarchartimiz;
    MainController mainController;
    private Parent root;
    private String chartTitle,xLabel;

    List<DataModel> dataModelList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startAnimation(){

        for(DataModel model : dataModelList){
            System.out.println(model.toString());
        }
        System.out.println("Date Models are ready");
        System.out.println("Title: " + chartTitle);
        System.out.println("xLabel: "+xLabel);
        System.out.println(dataModelList.size());

        //TODO Create series and give it to the barchart
    }

    public void setDataModelList(List<DataModel> dataModelList) {
        this.dataModelList = dataModelList;
    }

    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }
}
