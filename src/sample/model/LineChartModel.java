package sample.model;

import sample.Chart;
import sample.Line;

import java.util.ArrayList;
import java.util.List;


public class LineChartModel extends Chart {

    List<Line> lineList = new ArrayList<>();
    String title, xAxisLabel;

    //Default Constructor
    public LineChartModel() {
        super();
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



}
