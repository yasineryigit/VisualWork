package sample;

import java.time.LocalDate;

public class Line implements Comparable<Line> {

    private String key, country, name, category;
    private int value;
    private LocalDate localDate;

    public Line() {

    }

    public Line(String key, String country, String name, String category, int value, LocalDate localDate) {
        this.key = key;
        this.country = country;
        this.name = name;
        this.category = category;
        this.value = value;
        this.localDate = localDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public LocalDate getLocalDate() {
        return localDate;
    }

    public void setLocalDate(LocalDate localDate) {
        this.localDate = localDate;
    }

    @Override
    public int compareTo(Line o) {
        return 0;
    }

    @Override
    public String toString() {
        return "Line{" +
                "key='" + key + '\'' +
                ", country='" + country + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", value=" + value +
                ", localDate=" + localDate +
                '}';
    }
}
