package sample;

import java.time.LocalDate;

public class Bar implements Comparable<Bar>{

    private String key, name, category, country;
    private int value;
    private LocalDate localDate;


    public Bar() {
    }

    public Bar(String key, String name, String category, String country, int value, LocalDate localDate) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.country = country;
        this.value = value;
        this.localDate = localDate;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
    public String toString() {
        return "Bar{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", country='" + country + '\'' +
                ", value=" + value +
                ", date=" + localDate +
                '}';
    }

    @Override
    public int compareTo(Bar o) {
        return 0;
    }
}
