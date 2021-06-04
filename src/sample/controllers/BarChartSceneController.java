package sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
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
    @FXML
    private CheckBox checkBoxShowHide;


    int i, seriesIndex;
    Timeline tl = new Timeline();
    ObservableList<String> names = FXCollections.observableArrayList();
    ObservableList<Integer> values = FXCollections.observableArrayList();
    ObservableList<Integer> years = FXCollections.observableArrayList();
    ObservableList<String> categories = FXCollections.observableArrayList();

    public String getRandomRGB() {
        double r = Math.floor(Math.random() * 255);
        double g = Math.floor(Math.random() * 255);
        double b = Math.floor(Math.random() * 255);
        return r + "," + g + "," + b;
    }

    public void drawGraphic(BarChartModel barChartModel) {
        System.out.println("Gelen obje sayisi: " + barChartModel.getBarList().size());
        List<String> namesList = new ArrayList<>();
        List<String> categoriesList = new ArrayList<>();
        List<String> colorRGB = new ArrayList<>();
        String rgb = "";
        labelTitle.setText(barChartModel.getTitle());//title'ı yaz
        barGraphic.getYAxis().setLabel(barChartModel.getxAxisLabel());//axis labeli yaz

        //seride kullanmak üzere names, values, years listelerini doldurur
        for (Bar bar : barChartModel.getBarList()) {
            names.add(bar.getName());
            values.add(bar.getValue());
            years.add((bar.getLocalDate().getYear()));
            categories.add(bar.getCategory());

            if (!namesList.contains(bar.getName())) {//her bir ismi diziye 1 kez at
                namesList.add(bar.getName());
            }

            if (!categoriesList.contains(bar.getCategory())) {
                categoriesList.add(bar.getCategory());
            }
        }

        //Kaç tane name varsa o kadar seri oluştur
        XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(namesList.size()).toArray(XYChart.Series[]::new);

        //barGraphic içine tüm serileri ekle
        for (int i = 0; i < namesList.size(); i++) {
            barGraphic.getData().addAll(series[i]);
            //series[i].setName(namesList.get(i));////TODO colors,names will be set by category
        }

        for (int i = 0; i < categoriesList.size(); i++) {
            rgb = getRandomRGB();
            colorRGB.add(rgb);
        }

        for (int i = 0; i < series.length; i++) {
            int j = i;
            Platform.runLater(()
                    -> {
                Set<Node> nodes = barGraphic.lookupAll(".series" + j);
                for (Node n : nodes) {
                    n.setStyle("-fx-background-color: rgb(" + colorRGB.get(categoriesList.indexOf(barChartModel.getBarList().get(j).getCategory())) + ");" +
                            "-fx-bar-fill : rgb(" + colorRGB.get(categoriesList.indexOf(barChartModel.getBarList().get(j).getCategory())) + ")");
                }
            });
            series[i].setName( namesList.get(i) + " / " +  (categoriesList.get(categoriesList.indexOf(barChartModel.getBarList().get(i).getCategory()))));
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
                                        buttonPause.setDisable(true);//animasyon bitince buttonPause'u disable yap
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
        barGraphic.setLegendVisible(false);//hide legend at the beginning

        checkBoxShowHide.selectedProperty().addListener(new ChangeListener<Boolean>() {
            @Override
            public void changed(ObservableValue<? extends Boolean> observableValue, Boolean aBoolean, Boolean t1) {
                if (t1) {
                    barGraphic.setLegendVisible(true);
                    checkBoxShowHide.setText("Hide Legend");

                } else {
                    checkBoxShowHide.setText("Show Legend");
                    barGraphic.setLegendVisible(false);

                }
            }
        });
    }


    public void startAnimation() {

        buttonStart.setDisable(true);
        buttonPause.setDisable(false);
        buttonReload.setDisable(false);
        sortByLocalDate(barChartModel.getBarList());
        drawGraphic(barChartModel);
    }


    public void pauseAnimation() {
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
        tl.stop();
        tl.getKeyFrames().clear();
        i = 0;
        seriesIndex = 0;
        barGraphic.getData().clear();
        buttonStart.setDisable(false);
        buttonReload.setDisable(true);
        buttonPause.setText("PAUSE");
        buttonPause.setDisable(true);
        startAnimation();
    }



    public void setBarChart(BarChartModel barChartModel) {
        this.barChartModel = barChartModel;
    }

    //pencere kapatılırsa time'line ı durdur
    public void exit() {
        tl.stop();
    }
}
