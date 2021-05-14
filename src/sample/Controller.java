package sample;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.crypto.Data;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class Controller implements Initializable {

    @FXML
    private Label labelPath;
    @FXML
    private Button buttonSelect, buttonStartAnimation;
    @FXML
    private ComboBox<String> comboBoxDataSource;
    @FXML
    private ComboBox comboBoxAnimationType;
    @FXML
    private TextField textFieldUrl;
    List<DataModel> dataModelList = new ArrayList<>();
    DataModel model = new DataModel();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        buttonStartAnimation.setVisible(false);
        buttonSelect.setVisible(false);
        comboBoxAnimationType.setVisible(false);
        textFieldUrl.setVisible(false);

        comboBoxlariHazirla();


    }


    public void selectFile() {//Buton adı "Select File" ise dosya yolu ile parse metodu çağır, "Load Data" ise girilen url ile parse metodu çağır.

        if (buttonSelect.getText().equals("Select File")) {

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
                System.out.println(file.toURI() + " ile parsing'e gitti");
                List<DataModel> geciciListe =  parseLocalXMLToObject(String.valueOf(file.toURI()));
                System.out.println("Size: " + geciciListe.size());
                for(DataModel model : geciciListe){
                    System.out.println(model.toString());
                }
            }

        } else {
            System.out.println(textFieldUrl.getText() + " ile parsing'e gitti");
            parseOnlineXMLToObject(textFieldUrl.getText());

        }

    }

    private void parseOnlineXMLToObject(String text) {


        try {
            DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
            URL url = new URL(text);//Stringi URL'e çevirdik
            Document doc = builder.parse(url.openStream());


        } catch (Exception e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
        }


    }


    private List<DataModel> parseLocalXMLToObject(String uri) {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(uri);//dokümanı tanımladık
            NodeList recordList = doc.getElementsByTagName("record");//record tagiyle tutulan tüm elemanları recordList'e at
            for (int i = 0; i < recordList.getLength(); i++) {
                Node r = recordList.item(i); //her bir recordList elemanını
                if (r.getNodeType() == Node.ELEMENT_NODE) { //her node bir element ise
                    Element record = (Element) r;        //node'u record elementine at

                    System.out.println();
                    NodeList nameList = record.getChildNodes();
                    for (int j = 0; j < nameList.getLength(); j++) {
                        Node n = nameList.item(j);
                        if (n.getNodeType() == Node.ELEMENT_NODE) {
                            Element name = (Element) n;

                            model.setKey(((Element) n).getAttribute("key"));

                            if (((Element) n).getAttribute("name").equals("Name")) {
                                model.setName(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Country")) {
                                model.setCountry(n.getTextContent());
                            }
                            if (((Element) n).getAttribute("name").equals("Year")) {
                                model.setYear(Integer.parseInt(n.getTextContent()));
                            }
                            if (((Element) n).getAttribute("name").equals("Value")) {
                                model.setValue(Double.parseDouble(n.getTextContent()));
                            }
                            if (((Element) n).getAttribute("name").equals("Category")) {
                                model.setCategory(n.getTextContent());
                            }
                        }
                    }
                    dataModelList.add(model);
                }
            }

        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }
        return dataModelList;
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

    public void startAnimation() {

    }

}
