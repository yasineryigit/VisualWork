package sample.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import sample.Bar;
import sample.model.BarChartModel;

import java.net.URL;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class BarChartSceneController implements Initializable {


    private BarChartModel barChartModel = new BarChartModel();
    @FXML
    private BarChart<String, Integer> barGraphic;
    @FXML
    private CategoryAxis categoryAxis;
    @FXML
    private Label labelTitle;

    private ObservableList<String> countries = FXCollections.observableArrayList();
    private ObservableList<Integer> values = FXCollections.observableArrayList();
    private ObservableList<String> years = FXCollections.observableArrayList();


    public void drawGraphic(BarChartModel barChartModel){
        //labelTitle.setText(barChartModel.getTitle());
        for(Bar bar : barChartModel.getBarList()){//seride kullanmak üzere countries, values, years listelerini doldurur
            countries.add(bar.getCountry());
            values.add(bar.getValue());
            if(!years.contains(String.valueOf(bar.getValue()))){
                years.add(String.valueOf(bar.getYear()));
            }
            barGraphic.setTitle(String.valueOf(bar.getYear()));//yılı yazdır
        }
        XYChart.Series<String,Integer> series = new XYChart.Series<>();// seri objesi oluşturup içine country ve values objelerini attık
        for (int i = 0; i < barChartModel.getBarList().size(); i++) {
            series.getData().add(new XYChart.Data<>(countries.get(i), Integer.parseInt(values.get(i).toString())));
        }
        barGraphic.getData().add(series);//seriyi grafiğe bağladık
        categoryAxis.setCategories(years);

    }

    private void sortByYear(List<Bar> barList) {
        Collections.sort(barList, new Comparator<Bar>() {
            @Override
            public int compare(Bar o1, Bar o2) {
                return o1.getYear()-(o2.getYear());
            }
        });

    }


        @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void startAnimation() {

        /*
        System.out.println("\n\n\n\n\n\n\n\n");
        sortByYear(barChartModel.getBarList());//listeyi yıla göre sırala
        for (Bar barModel : barChartModel.getBarList()) {
            System.out.println(barModel.toString());
        }
        System.out.println("Date Models are ready and sorted");
        System.out.println("Title: " + barChartModel.getTitle());
        System.out.println("xLabel: " + barChartModel.getxAxisLabel());
        System.out.println("Size: " + barChartModel.getBarList().size());
        */
        sortByYear(barChartModel.getBarList());//listeyi yıla göre sırala
        drawGraphic(barChartModel);

    }


    public void setBarChart(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }
}
