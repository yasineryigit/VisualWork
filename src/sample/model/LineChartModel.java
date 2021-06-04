package sample.model;

import sample.Chart;
import sample.Line;

import java.util.ArrayList;
import java.util.List;


public class LineChartModel extends Chart {

    List<Line> lineList = new ArrayList<>();
    private String title, xAxisLabel;

    public LineChartModel() {
        super("","");
    }

    public LineChartModel(String title, String xAxisLabel) {
        super(title, xAxisLabel);
    }

    public LineChartModel(List<Line> lineList, String title, String xAxisLabel) {
        super(title, xAxisLabel);
        this.lineList = lineList;
        this.title = title;
        this.xAxisLabel = xAxisLabel;
    }

    public List<Line> getLineList() {
        return lineList;
    }


    @Override
    public void setCaption(String caption) {

    }

    @Override
    public void reset() {

    }
}
