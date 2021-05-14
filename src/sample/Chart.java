package sample;

public abstract class Chart {

    private String title;
    private String xAxisLabel;


    public Chart(String title, String xAxisLabel) {
        this.title = title;
        this.xAxisLabel = xAxisLabel;
    }

    public abstract void setCaption(String caption); //chart'ın açıklamasını değiştirir
    public abstract void reset();   //chart'tan tüm verileri siler


}
