package sample;

import java.util.concurrent.ConcurrentMap;

public class Line implements Comparable<Line> {

    private String name, category;
    private int value;

    public Line(String name, String category, int value) {
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

    public int getValue() {
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
