package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import sample.Bar;
import sample.BarChartModel;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.DoubleBinaryOperator;

public class BarChartSceneController implements Initializable {


    private BarChartModel barChartModel = new BarChartModel();
    @FXML
    private BarChart<String, Integer> barGraphic;
    @FXML
    private CategoryAxis categoryAxis;
    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<Integer> values = FXCollections.observableArrayList();
    private ObservableList<String> years = FXCollections.observableArrayList();

    public void drawGraphic(BarChartModel barChartModel){
        for(Bar bar : barChartModel.getBarList()){
            countries.add(bar.getCountry());
            values.add(bar.getValue());
            if(!years.contains(String.valueOf(bar.getValue()))){
                years.add(String.valueOf(bar.getYear()));
            }
        }
        XYChart.Series<String,Integer> series = new XYChart.Series<>();// seri objesi oluşturduk
        for (int i = 0; i < barChartModel.getBarList().size(); i++) {
            series.getData().add(new XYChart.Data<String, Integer>(countries.get(i),Integer.parseInt(values.get(i).toString())));
        }
        barGraphic.getData().add(series);
        categoryAxis.setCategories(years);

    }



    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startAnimation() {

        for (Bar barModel : barChartModel.getBarList()) {
            System.out.println(barModel.toString());
        }
        System.out.println("Date Models are ready");
        System.out.println("Title: " + barChartModel.getTitle());
        System.out.println("xLabel: " + barChartModel.getxAxisLabel());
        System.out.println("Size: " + barChartModel.getBarList().size());

        drawGraphic(barChartModel);
    }


    public void setBarChart(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }
}
