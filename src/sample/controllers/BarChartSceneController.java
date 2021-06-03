package sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import sample.Bar;
import sample.model.BarChartModel;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;

public class BarChartSceneController implements Initializable {


    private BarChartModel barChartModel = new BarChartModel();
    @FXML
    private BarChart<String, Integer> barGraphic;
    @FXML
    private Label labelTitle;
    @FXML
    private Button buttonStart, buttonPause, buttonReload;

    int i, seriesIndex;
    Timeline tl = new Timeline();
    ObservableList<String> names = FXCollections.observableArrayList();
    ObservableList<Integer> values = FXCollections.observableArrayList();
    ObservableList<Integer> years = FXCollections.observableArrayList();

    public void drawGraphic(BarChartModel barChartModel) {
        System.out.println("Gelen obje sayisi: " + barChartModel.getBarList().size());
        List<String> namesList = new ArrayList<>();
        labelTitle.setText(barChartModel.getTitle());//title'ı yaz
        barGraphic.getYAxis().setLabel(barChartModel.getxAxisLabel());//axis labeli yaz

        //seride kullanmak üzere names, values, years listelerini doldurur
        for (Bar bar : barChartModel.getBarList()) {
            names.add(bar.getName());
            values.add(bar.getValue());
            years.add((bar.getLocalDate().getYear()));

            if (!namesList.contains(bar.getName())) {//her bir ismi diziye 1 kez at
                namesList.add(bar.getName());
            }
        }

        //Kaç tane name varsa o kadar seri oluştur
        XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(namesList.size()).toArray(XYChart.Series[]::new);

        //barGraphic içine tüm serileri ekle
        for (int i = 0; i < namesList.size(); i++) {
            barGraphic.getData().addAll(series[i]);
            //series[i].setName(namesList.get(i));////TODO colors,names will be set by category
        }


        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(10),//10ms'de bir
                        new EventHandler<>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try {
                                    if (i < barChartModel.getBarList().size()) {

                                        barGraphic.setTitle(String.valueOf(barChartModel.getBarList().get(i).getLocalDate()));//yılı yazdırır
                                        seriesIndex = namesList.indexOf(barChartModel.getBarList().get(i).getName());
                                        series[seriesIndex].getData().add(new XYChart.Data<>(names.get(i), values.get(i)));


                                        i++;
                                    } else {
                                        tl.stop();//hepsini çizince timeline'ı durdur
                                        System.out.println("Time line stopped");
                                    }
                                } catch (Exception e) {
                                    System.out.println(e.getMessage());
                                }

                            }
                        }
                ));
        tl.setCycleCount(Animation.INDEFINITE);
        tl.setAutoReverse(true);
        tl.play();
    }


    private void sortByLocalDate(List<Bar> barList) {//listedeki elemanları localDate değişkenine göre sıralar
        Collections.sort(barList, new Comparator<Bar>() {
            @Override
            public int compare(Bar o1, Bar o2) {
                return o1.getLocalDate().compareTo(o2.getLocalDate());
            }
        });
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonPause.setDisable(true);
        buttonReload.setDisable(true);
    }


    public void startAnimation() {

        buttonStart.setDisable(true);
        buttonPause.setDisable(false);
        buttonReload.setDisable(false);
        sortByLocalDate(barChartModel.getBarList());
        drawGraphic(barChartModel);
    }


    public void stopAnimation() {
        buttonStart.setDisable(true);
        buttonReload.setDisable(false);
        if (buttonPause.getText().equals("CONTINUE")) {
            tl.play();
            buttonPause.setText("PAUSE");
        } else {
            tl.stop();
            buttonPause.setText("CONTINUE");
        }
    }

    public void reloadAnimation(ActionEvent e) {
        //TODO

    }


    public void setBarChart(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    @FXML
    public void exitApplication(ActionEvent event) {
        tl.stop();
        Platform.exit();

    }
}
