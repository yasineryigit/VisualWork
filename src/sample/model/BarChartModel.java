package sample.model;

import sample.Bar;
import sample.Chart;

import java.util.ArrayList;
import java.util.List;

public class BarChartModel extends Chart {

    List<Bar> barList = new ArrayList<>();
    String title, xAxisLabel;

    //Default constructor
    public BarChartModel() {
        super();
    }

    public BarChartModel(List<Bar> barList, String title, String xAxisLabel) {
        super(title, xAxisLabel);
        this.barList = barList;
        this.title = title;
        this.xAxisLabel = xAxisLabel;
    }

    public List<Bar> getBarList() {
        return barList;
    }




    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void reset() {

    }
}
