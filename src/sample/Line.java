package sample;

public class Line implements Comparable<Line> {

    private String name, category;
    private double value;

    public Line(String name, String category, double value) {
        this.name = name;
        this.category = category;
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getValue() {
        return value;
    }

    public int nextValue(){//line'ın sıradaki value'sini döndürür
        return 0;
    }

    @Override
    public int compareTo(Line o) {
        return 0;
    }
}
