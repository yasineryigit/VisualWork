package sample.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import sample.Bar;
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
    private String chartTitle, xLabel;
    BarChart barChart = new BarChart();

    List<DataModel> dataModelList = new ArrayList<>();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startAnimation() {

        for (Bar barModel : barChart.getBarList()) {
            System.out.println(barModel.toString());
        }
        System.out.println("Date Models are ready");
        System.out.println("Title: " + barChart.getTitle());
        System.out.println("xLabel: " + barChart.getxAxisLabel());
        System.out.println("Size: " + barChart.getBarList().size());

        //TODO Create Series
    }


    public void setChartTitle(String chartTitle) {
        this.chartTitle = chartTitle;
    }

    public void setxLabel(String xLabel) {
        this.xLabel = xLabel;
    }

    public void setBarChart(BarChart barChart) {
        this.barChart = barChart;
    }
}
