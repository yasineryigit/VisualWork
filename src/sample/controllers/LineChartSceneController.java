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
import javafx.scene.Node;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.util.Duration;
import sample.Line;
import sample.model.LineChartModel;

import java.net.URL;
import java.util.*;
import java.util.stream.Stream;


public class LineChartSceneController implements Initializable {

    private LineChartModel lineChartModel = new LineChartModel();

    @FXML
    private LineChart<String, Integer> lineGraphic;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private Label labelTitle;

    @FXML
    private Button buttonStart, buttonPause, buttonReload;

    Timeline tl = new Timeline();
    int i = 0;
    int seriesIndex = 0;

    private ObservableList<String> names = FXCollections.observableArrayList();
    private ObservableList<Integer> values = FXCollections.observableArrayList();
    private ObservableList<String> years = FXCollections.observableArrayList();
    private ObservableList<String> categories = FXCollections.observableArrayList();

    public String getRandomRGB() {
        double r = Math.floor(Math.random() * 255);
        double g = Math.floor(Math.random() * 255);
        double b = Math.floor(Math.random() * 255);
        return r + "," + g + "," + b;
    }

    public void drawGraphic(LineChartModel lineChartModel) {
        System.out.println("Gelen obje sayisi: " + lineChartModel.getLineList().size());

        List<String> namesList = new ArrayList<>();
        List<String> categoriesList = new ArrayList<>();
        List<String> colorRGB = new ArrayList<>();
        String rgb = "";


        labelTitle.setText(lineChartModel.getTitle());//title'ı yaz
        lineGraphic.getYAxis().setLabel(lineChartModel.getxAxisLabel());//axis labeli yaz

        for (Line line : lineChartModel.getLineList()) {
            names.add(line.getName());
            values.add(line.getValue());
            years.add(String.valueOf(line.getLocalDate()));
            categories.add(line.getCategory());
            if (!namesList.contains(line.getName())) {
                namesList.add(line.getName());
            }
            if (!categoriesList.contains(line.getCategory())) {
                categoriesList.add(line.getCategory());
            }
        }

        XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(namesList.size()).toArray(XYChart.Series[]::new);


        for (int i = 0; i < namesList.size(); i++) {
            lineGraphic.getData().addAll(series[i]);
        }

        for (int i = 0; i < categoriesList.size(); i++) {
            rgb = getRandomRGB();
            colorRGB.add(rgb);
        }

        for (int i = 0; i < series.length; i++) {
            int j = i;
            Platform.runLater(()
                    -> {
                Set<Node> nodes = lineGraphic.lookupAll(".series" + j);
                for (Node n : nodes) {
                    n.setStyle("-fx-background-color: rgb(" + colorRGB.get(categoriesList.indexOf(lineChartModel.getLineList().get(j).getCategory())) + ");" +
                            "-fx-stroke: rgb(" + colorRGB.get(categoriesList.indexOf(lineChartModel.getLineList().get(j).getCategory())) + ")");
                }
            });
            series[i].setName( namesList.get(i) + " / " +  (categoriesList.get(categoriesList.indexOf(lineChartModel.getLineList().get(i).getCategory()))));
        }


        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(10),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    if (i < lineChartModel.getLineList().size()) {
                                        seriesIndex = namesList.indexOf(lineChartModel.getLineList().get(i).getName());
                                        series[seriesIndex].getData().add(new XYChart.Data<>(years.get(i), values.get(i)));
                                        i++;
                                    } else {
                                        tl.stop();
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

    private void sortByLocalDate(List<Line> lineList) {//listedeki elemanları localDate değişkenine göre sıralar
        Collections.sort(lineList, new Comparator<Line>() {
            @Override
            public int compare(Line o1, Line o2) {
                return o1.getLocalDate().compareTo(o2.getLocalDate());
            }
        });
    }

    public void startAnimation() {
        buttonStart.setDisable(true);
        buttonPause.setDisable(false);
        buttonReload.setDisable(false);
        sortByLocalDate(lineChartModel.getLineList());
        drawGraphic(lineChartModel);
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
        i=0;
        seriesIndex=0;
        lineGraphic.getData().clear();
        buttonStart.setDisable(false);
        buttonReload.setDisable(true);
        buttonPause.setText("PAUSE");
        buttonPause.setDisable(true);
        startAnimation();
    }

    public void setLineChart(LineChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonPause.setDisable(true);
        buttonReload.setDisable(true);
    }

    //pencere kapatılırsa time'line ı durdur
    public void exit() {
        tl.stop();
    }

}