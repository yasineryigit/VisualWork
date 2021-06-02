package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.Bar;
import sample.Line;
import sample.model.BarChartModel;
import sample.model.LineChartModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Scanner;

public class MainController implements Initializable {

    @FXML
    private Label labelPath;
    @FXML
    private Button buttonSelect, buttonStartAnimation;

    @FXML
    private ComboBox<String> comboBoxAnimationType;
    @FXML

    String chartTitle, xLabel;

    BarChartModel barChartModel;
    List<Bar> barList = new ArrayList<>();

    LineChartModel lineChartModel;
    List<Line> lineList = new ArrayList<>();


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonStartAnimation.setVisible(false);
        buttonSelect.setVisible(false);
        comboBoxAnimationType.setVisible(false);


        comboBoxlariHazirla();


    }


    public void selectFile(ActionEvent e) throws IOException {//Buton adı "Select File" ise dosya yolu ile parse metodu çağır, "Load Data" ise girilen url ile parse metodu çağır.
        //seçme ekranına gittiyse listeleri temizle
        barList.clear();
        lineList.clear();

        if (buttonSelect.getText().equals("Select File")) {//select file butonuna tıklanınca

            FileChooser fileChooser = new FileChooser();

            fileChooser.setInitialDirectory(new File("./inputs"));//dosya seçme başlanguç konumunu proje dizini yapar

            fileChooser.getExtensionFilters().addAll( // Dosya türü filtreleme eklendi
                    new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml"),
                    new FileChooser.ExtensionFilter("TEXT files (*.txt)", "*.txt")
            );


            File file = fileChooser.showOpenDialog(null);  // Dosya seçme ekranı aç
            if (file != null) {
                labelPath.setText(file.getPath());
                buttonStartAnimation.setVisible(true);
                comboBoxAnimationType.setVisible(true);

                String extension = file.getAbsolutePath().substring(file.getAbsolutePath().lastIndexOf("."));
                System.out.println("Extension: " + extension);


                if (extension.equals(".xml")) {//xml uzantılıysa
                    parseLocalXMLToObject(String.valueOf(file.toURI()));//verilen dosyayı parse et

                } else if (extension.equals(".txt")) {//txt uzantılıysa
                    parseLocalTXTToObject(file);

                } else {//Belirsiz dosya türüyse
                    System.out.println("Belirsiz uzantılı");
                    //TODO Alert gösterilebilir
                }

                barChartModel = new BarChartModel(barList, chartTitle, xLabel); //eklenen verilerle barChart objesi oluştur
                lineChartModel = new LineChartModel(lineList, chartTitle, xLabel);//eklenen verilerle lineChart objesi oluştur
                /*
                //TESTING
                for (Bar barModel : barChartModel.getBarList()) {
                    System.out.println(barModel.toString());
                }
                System.out.println("Bunlarla grafik çizdirilecek");
                System.out.println(barList.size());*/

            }
        }
    }

    //TODO gelen verilerle (localDate, name, value..) bar objesi oluşturulacak ve barList'e eklenecek
    private void parseLocalTXTToObject(File file) throws IOException {
        Scanner scanTxtFile = new Scanner(file);

        chartTitle = Files.readAllLines(Paths.get(file.toURI())).get(0); // 1. satırdaki cümlemizi "chartTitle" değişkenine atadık.
        System.out.println("chartTitle: " + chartTitle); // TEST

        xLabel = Files.readAllLines(Paths.get(file.toURI())).get(1); // 2. satırdaki cümlemizi "xLabel" değişkenine atadık.
        System.out.println("xLabel: " + xLabel); // TEST

        while (scanTxtFile.hasNextLine()) {
            String nextLine = scanTxtFile.nextLine();
            if ((nextLine.indexOf(',')) != -1) { // Sadece dataları almak için yapılan işlem. Burada satırımızda virgül yoksa o satır atlanıyor.
                Bar bar = new Bar();
                Line line = new Line();

                String[] splitTxtFile = nextLine.split(","); // Satırdaki verileri virgüllere göre parçalıyor.


                LocalDate date;

                if (splitTxtFile[0].contains("-")) {//brand values için date oluştur
                    //parçala LocalDate date = LocalDate.of(Integer.parseInt(splitTxtFile[0]), 1, 1);
                    String[] temp = splitTxtFile[0].split("\\-");
                    int year = Integer.parseInt(temp[0]);
                    int month = Integer.parseInt(temp[1]);
                    int day = Integer.parseInt(temp[2]);
                    date = LocalDate.of(year, month, day);
                    //System.out.println("Year: " + year +" Month:"+ month + " Day: "+day);//TEST

                } else {//city pupulation için date oluştur
                    date = LocalDate.of(Integer.parseInt(splitTxtFile[0]), 1, 1);
                }

                bar.setLocalDate(date);
                line.setLocalDate(date);

                bar.setName(splitTxtFile[1]);
                line.setName(splitTxtFile[1]);

                bar.setCountry(splitTxtFile[2]);
                line.setCountry(splitTxtFile[2]);


                bar.setValue(Integer.parseInt(splitTxtFile[3]));
                line.setValue(Integer.parseInt(splitTxtFile[3]));

                bar.setCategory(splitTxtFile[4]);
                line.setCategory(splitTxtFile[4]);


                barList.add(bar);
                lineList.add(line);

            }
        }


    }


    private void parseLocalXMLToObject(String uri) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri);//dokümanı tanımladık
            NodeList recordList = doc.getElementsByTagName("record");//record tagiyle tutulan tüm elemanları recordList'e at
            chartTitle = doc.getElementsByTagName("title").item(0).getTextContent();
            xLabel = doc.getElementsByTagName("xlabel").item(0).getTextContent();

            //tüm nodları gez
            for (int i = 0; i < recordList.getLength(); i++) {
                Bar bar = new Bar();
                Line line = new Line();
                Node r = recordList.item(i); //her bir recordList elemanını
                if (r.getNodeType() == Node.ELEMENT_NODE) { //her node bir element ise
                    Element record = (Element) r;        //node'u record elementine at
                    //System.out.println();
                    NodeList nameList = record.getChildNodes();
                    //bir node içindeki değişkenleri kullanarak obje oluştur
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {


                            if (!((Element) n).getAttribute("key").equals("")) {
                                bar.setKey(((Element) n).getAttribute("key"));
                                line.setKey(((Element) n).getAttribute("key"));
                            }
                            if (((Element) n).getAttribute("name").equals("Name")) {
                                bar.setName(n.getTextContent());
                                line.setName(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Country")) {
                                bar.setCountry(n.getTextContent());
                                line.setCountry(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Year")) {
                                LocalDate date = LocalDate.of(Integer.parseInt(n.getTextContent()), 1, 1);
                                bar.setLocalDate(date);
                                line.setLocalDate(date);
                            }
                            if (((Element) n).getAttribute("name").equals("Value")) {
                                bar.setValue(Integer.parseInt(n.getTextContent()));
                                line.setValue(Integer.parseInt(n.getTextContent()));
                            }
                            if (((Element) n).getAttribute("name").equals("Category")) {
                                bar.setCategory(n.getTextContent());
                                line.setCategory(n.getTextContent());
                            }

                        }
                    }
                }
                barList.add(bar);
                lineList.add(line);
            }
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }


    public void startAnimationScene(ActionEvent event) {
        String animationType = comboBoxAnimationType.getValue();


        if (!comboBoxAnimationType.getSelectionModel().isEmpty()) {//comboBoxAnimationType'dan bir seçim yapıldıysa
            if (animationType.equals("Bar Chart")) {

                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/BarChartScene.fxml"));
                    Parent root = loader.load();

                    BarChartSceneController barChartSceneController = loader.getController();

                    barChartSceneController.setBarChart(barChartModel);// BarSceneController'a barChartModel'i gönderdik

                    Stage stage = new Stage();
                    stage.setTitle("Bar Chart");
                    stage.setScene(new Scene(root));
                    stage.show();//BarChartScene'i aç


                    // Hide this current window (if this is what you want)
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (animationType.equals("Line Chart")) {
                try {

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/LineChartScene.fxml"));
                    Parent root = loader.load();

                    LineChartSceneController lineChartSceneController = loader.getController();

                    lineChartSceneController.setLineChart(lineChartModel);

                    Stage stage = new Stage();
                    stage.setTitle("Line Chart");
                    stage.setScene(new Scene(root));
                    stage.show();
                    // Hide this current window (if this is what you want)
                    //((Node)(event.getSource())).getScene().getWindow().hide();
                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println(e.getMessage());
                }
                //  lineList.clear();
            }

        } else {//bir seçim yppılmadıysa uyarı göster

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setHeaderText("You need to select an animation type");
            alert.setContentText("Please select an animation type for start animation");
            alert.showAndWait();
        }


    }


    private void comboBoxlariHazirla() {

        ObservableList<String> animationMenulist = comboBoxAnimationType.getItems();
        animationMenulist.add("Bar Chart");
        animationMenulist.add("Line Chart");

        buttonSelect.setVisible(true);
        buttonSelect.setText("Select File");

    } //local veya internet seçeneklerine göre görünümü değiştirir


}
