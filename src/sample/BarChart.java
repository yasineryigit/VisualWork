package sample;

import java.util.ArrayList;
import java.util.List;

public class BarChart extends Chart {

    List<Bar> barList = new ArrayList<>();
    private String title, xAxisLabel;

    public BarChart() {
        super("","");
    }

    public BarChart(String title, String xAxisLabel) {
        super(title, xAxisLabel);
    }

    public BarChart(List<Bar> barList, String title, String xAxisLabel) {
        super(title, xAxisLabel);
        this.barList = barList;
        this.title=title;
        this.xAxisLabel=xAxisLabel;
    }

    public List<Bar> getBarList() {
        return barList;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getxAxisLabel() {
        return xAxisLabel;
    }

    public void setxAxisLabel(String xAxisLabel) {
        this.xAxisLabel = xAxisLabel;
    }

    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void reset() {

    }
}
