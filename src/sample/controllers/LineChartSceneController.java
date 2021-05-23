package sample.controllers;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
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
    private Button buttonStart,buttonStop,buttonReload;

    //XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(22).toArray(XYChart.Series[]::new);
    Timeline tl = new Timeline();
    int i = 0;
    int seriesIndex = 0;

    private ObservableList<String> names = FXCollections.observableArrayList();
    private ObservableList<Integer> values = FXCollections.observableArrayList();
    private ObservableList<String> years = FXCollections.observableArrayList();

    public void drawGraphic(LineChartModel lineChartModel) {
        System.out.println("Gelen obje sayisi: " + lineChartModel.getLineList().size());
        List<String> namesArray = new ArrayList<>();

        labelTitle.setText(lineChartModel.getTitle());//title'ı yaz
        lineGraphic.getYAxis().setLabel(lineChartModel.getxAxisLabel());//axis labeli yaz

        for (Line line : lineChartModel.getLineList()) {
            names.add(line.getCountry());
            values.add(line.getValue());
            years.add(String.valueOf(line.getLocalDate()));

            if (!namesArray.contains(line.getName())) {
                namesArray.add(line.getName());
            }
        }

        XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(namesArray.size()).toArray(XYChart.Series[]::new);

        for (int i = 0; i < namesArray.size(); i++) {
            lineGraphic.getData().addAll(series[i]);
            series[i].setName(namesArray.get(i));
        }

        //Drawing without timeline
        /*for(counter = 0 ; counter < lineChartModel.getLineList().size() ; counter++){
            seriesIndex = namesArray.indexOf(lineChartModel.getLineList().get(counter).getCountry());
            series[seriesIndex].getData().add(new XYChart.Data<>(years.get(counter),values.get(counter)));
        }*/

        /*  for (Line line : lineChartModel.getLineList()) {
            if (counter < lineChartModel.getLineList().size()) {
                //lineGraphic.setTitle(String.valueOf(lineChartModel.getLineList().get(counter).getYear()));
                seriesIndex = namesArray.indexOf(lineChartModel.getLineList().get(counter).getCountry());
                try {
                    series[seriesIndex].getData().add(new XYChart.Data<>(years.get(counter), values.get(counter)));
                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                }
                counter++;
        }*/

        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(10),
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent event) {
                                try {
                                    if (i < lineChartModel.getLineList().size()) {
                                        //lineGraphic.setTitle(String.valueOf(lineChartModel.getLineList().get(i).getYear()));
                                        seriesIndex = namesArray.indexOf(lineChartModel.getLineList().get(i).getName());
                                        series[seriesIndex].getData().add(new XYChart.Data<>(years.get(i), values.get(i)));
                                        i++;
                                    }
                                    else{
                                        tl.stop();
                                    }
                                }catch (Exception e) {
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
        buttonStop.setDisable(false);
        buttonReload.setDisable(false);
        sortByLocalDate(lineChartModel.getLineList());
        drawGraphic(lineChartModel);
    }

    public void stopAnimation(){
        buttonStart.setDisable(true);
        buttonReload.setDisable(false);
        if(buttonStop.getText().equals("CONTINUE")){
            tl.play();
            buttonStop.setText("PAUSE");
        }
        else{
            tl.stop();
            buttonStop.setText("CONTINUE");
        }
    }

    public void reload(){
        //tl.getKeyFrames().clear();
        //tl.getKeyFrames().removeAll();
       /* lineGraphic.getData().clear();
        countries.clear();
        years.clear();
        values.clear();
        for(int i = 0 ; i < series.length ; i++){
            series[i].getData().clear();
        }
        counter = 0;
        tl.getKeyFrames().clear();
        sortByYear(lineChartModel.getLineList());
        drawGraphic(lineChartModel); */
        //startButton.setDisable(false);
        //stopButton.setDisable(true);
        //reloadButton.setDisable(true);
    }
    public void setLineChart(LineChartModel lineChartModel) {
        this.lineChartModel = lineChartModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        buttonStop.setDisable(true);
        buttonReload.setDisable(true);


    }

}