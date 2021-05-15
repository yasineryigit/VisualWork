package sample.controllers;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import sample.Bar;
import sample.model.BarChartModel;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private Label labelPath;
    @FXML
    private Button buttonSelect, buttonStartAnimation;
    @FXML
    private ComboBox<String> comboBoxDataSource;
    @FXML
    private ComboBox<String> comboBoxAnimationType;
    @FXML
    private TextField textFieldUrl;
    List<Bar> barList = new ArrayList<>();
    String chartTitle,xLabel;
    BarChartModel barChartModel;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonStartAnimation.setVisible(false);
        buttonSelect.setVisible(false);
        comboBoxAnimationType.setVisible(false);
        textFieldUrl.setVisible(false);

        comboBoxlariHazirla();


    }


    public void selectFile(ActionEvent e) {//Buton adı "Select File" ise dosya yolu ile parse metodu çağır, "Load Data" ise girilen url ile parse metodu çağır.

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
                parseLocalXMLToObject(String.valueOf(file.toURI()));//verilen dosyayı parse et

                barChartModel = new BarChartModel(barList,chartTitle,xLabel); //eklenen verilerle barChart objesi oluştur

                //TESTING
                for(Bar barModel : barChartModel.getBarList()){
                    System.out.println(barModel.toString());
                }
                System.out.println("Bunlarla grafik çizdirilecek");
                System.out.println(barList.size());
            }

        } else {//load data butonuna tıklanınca
            System.out.println(textFieldUrl.getText() + " ile parsing'e gitti");
            //parseOnlineXMLToObject(textFieldUrl.getText());/*TODO linkteki xml'e parsing yapma metodu eklenecek*/

        }

    }

    public void startAnimationScene(ActionEvent event) throws Exception{
        String animationType = (String) comboBoxAnimationType.getValue();
        System.out.println(animationType + " yapılacak");
        if(animationType.equals("Bar Chart")){

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../view/BarChartScene.fxml"));
                Parent root = loader.load();

                BarChartSceneController barChartSceneController = loader.getController();

                barChartSceneController.setBarChart(barChartModel);

                Stage stage = new Stage();
                stage.setTitle("Bar Chart");
                stage.setScene(new Scene(root));
                stage.show();
                // Hide this current window (if this is what you want)
                //((Node)(event.getSource())).getScene().getWindow().hide();
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }

    }


    private void parseLocalXMLToObject(String uri) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri);//dokümanı tanımladık
            NodeList recordList = doc.getElementsByTagName("record");//record tagiyle tutulan tüm elemanları recordList'e at
            chartTitle=doc.getElementsByTagName("title").item(0).getTextContent();
            xLabel = doc.getElementsByTagName("xlabel").item(0).getTextContent();

            //tüm nodları gez
            for (int i = 0; i < recordList.getLength(); i++) {
                Bar bar = new Bar();
                Node r = recordList.item(i); //her bir recordList elemanını
                if (r.getNodeType() == Node.ELEMENT_NODE) { //her node bir element ise
                    Element record = (Element) r;        //node'u record elementine at
                    //System.out.println();
                    NodeList nameList = record.getChildNodes();
                    //bir node içindeki değişkenleri kullanarak obje oluştur
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {


                            if(!((Element) n).getAttribute("key").equals("")){
                                bar.setKey(((Element) n).getAttribute("key"));
                            }
                            if (((Element) n).getAttribute("name").equals("Name")) {
                                bar.setName(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Country")) {
                                bar.setCountry(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Year")) {
                                bar.setYear(Integer.parseInt(n.getTextContent()));
                            }
                            if (((Element) n).getAttribute("name").equals("Value")) {
                                bar.setValue(Integer.parseInt(n.getTextContent()));
                            }
                            if (((Element) n).getAttribute("name").equals("Category")) {
                                bar.setCategory(n.getTextContent());
                            }

                        }
                    }
                }
                barList.add(bar);
            }

        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }



    private void comboBoxlariHazirla() {
        ObservableList<String> dataSourceMenulist = comboBoxDataSource.getItems();
        dataSourceMenulist.add("Local Data");
        dataSourceMenulist.add("Internet URL");

        ObservableList<String> animationMenulist = comboBoxAnimationType.getItems();
        animationMenulist.add("Bar Chart");
        animationMenulist.add("Line Chart");


        comboBoxDataSource.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println(comboBoxDataSource.valueProperty().get());
                if (comboBoxDataSource.valueProperty().get().equals("Local Data")) {
                    buttonSelect.setVisible(true);
                    textFieldUrl.setVisible(false);
                    buttonSelect.setText("Select File");
                } else {
                    labelPath.setText("");
                    textFieldUrl.setVisible(true);
                    buttonSelect.setVisible(true);
                    buttonSelect.setText("Load Data");
                    comboBoxAnimationType.setVisible(true);
                }

            }
        });
    } //local veya internet seçeneklerine göre görünümü değiştirir

    public void startAnimationScene() {

    }

}
