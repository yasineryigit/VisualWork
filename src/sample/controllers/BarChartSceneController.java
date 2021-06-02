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
import javafx.geometry.Bounds;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.text.Text;
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
    private CategoryAxis categoryAxis;
    @FXML
    private NumberAxis numberAxis;
    @FXML
    private Label labelTitle;
    @FXML
    private Button buttonStart, buttonStop, buttonReload;


    //XYChart.Series<String, Integer>[] series;
    int seriesIndex = 0;
    //XYChart.Series<String, Integer> series = new XYChart.Series<>();
    int i = 1;
    Timeline tl = new Timeline();
    ObservableList<String> names = FXCollections.observableArrayList();
    ObservableList<Integer> values = FXCollections.observableArrayList();
    ObservableList<Integer> years = FXCollections.observableArrayList();

    public void drawGraphic(BarChartModel barChartModel) {
        System.out.println("Gelen obje sayisi: " + barChartModel.getBarList().size());
        List<String> namesArray = new ArrayList<>();
        labelTitle.setText(barChartModel.getTitle());//title'ı yaz
        barGraphic.getYAxis().setLabel(barChartModel.getxAxisLabel());//axis labeli yaz

        //seride kullanmak üzere names, values, years listelerini doldurur
        for (Bar bar : barChartModel.getBarList()) {
            names.add(bar.getName());
            values.add(bar.getValue());
            years.add((bar.getLocalDate().getYear()));

            if (!namesArray.contains(bar.getName())) {//her bir ismi diziye 1 kez at
                namesArray.add(bar.getName());
            }
        }

        //Kaç tane name varsa o kadar seri oluştur
        XYChart.Series<String, Integer>[] series = Stream.<XYChart.Series<String, Integer>>generate(XYChart.Series::new).limit(namesArray.size()).toArray(XYChart.Series[]::new);

        //barGraphic içine tüm serileri ekle
        for (int i = 0; i < namesArray.size(); i++) {
            barGraphic.getData().addAll(series[i]);
            //series[i].setName(namesArray.get(i));
        }


        tl.getKeyFrames().add(
                new KeyFrame(Duration.millis(10),//10ms'de bir
                        new EventHandler<ActionEvent>() {
                            @Override
                            public void handle(ActionEvent actionEvent) {
                                try {
                                    if (i < barChartModel.getBarList().size()) {

                                        barGraphic.setTitle(String.valueOf(barChartModel.getBarList().get(i).getLocalDate()));//yılı yazdırır
                                        seriesIndex = namesArray.indexOf(barChartModel.getBarList().get(i).getName());
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


    //@TEST bar üstünde değerini yazdırmak için
    private void setNodeStyle(XYChart.Data<String, Integer> data) {
        Node node = data.getNode();
        if (data.getYValue().intValue() > 8) {
            node.setStyle("-fx-bar-fill: -fx-exceeded;");
        } else if (data.getYValue().intValue() > 5) {
            node.setStyle("-fx-bar-fill: -fx-achieved;");
        } else {
            node.setStyle("-fx-bar-fill: -fx-not-achieved;");
        }
    }

    //@TEST bar üstünde değerini yazdırmak için
    private void displayLabelForData(XYChart.Data<String, Integer> data) {
        final Node node = data.getNode();
        final Text dataText = new Text(data.getYValue() + "");
        node.parentProperty().addListener(new ChangeListener<Parent>() {
            @Override
            public void changed(ObservableValue<? extends Parent> ov, Parent oldParent, Parent parent) {
                Group parentGroup = (Group) parent;
                parentGroup.getChildren().add(dataText);
            }
        });

        node.boundsInParentProperty().addListener(new ChangeListener<Bounds>() {//yazının konumunu ayarlar
            @Override
            public void changed(ObservableValue<? extends Bounds> ov, Bounds oldBounds, Bounds bounds) {
                dataText.setLayoutX(
                        Math.round(
                                bounds.getMinX() + bounds.getWidth() / 2 - dataText.prefWidth(-1) / 2
                        )
                );
                dataText.setLayoutY(
                        Math.round(
                                bounds.getMinY() - dataText.prefHeight(-1) * 0.5
                        )
                );
            }
        });
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
        buttonStop.setDisable(true);
        buttonReload.setDisable(true);
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
        buttonStart.setDisable(true);
        buttonStop.setDisable(false);
        buttonReload.setDisable(false);
        sortByLocalDate(barChartModel.getBarList());
        drawGraphic(barChartModel);
    }


    public void stopAnimation() {
        buttonStart.setDisable(true);
        buttonReload.setDisable(false);
        if (buttonStop.getText().equals("CONTINUE")) {
            tl.play();
            buttonStop.setText("PAUSE");
        } else {
            tl.stop();
            buttonStop.setText("CONTINUE");
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
