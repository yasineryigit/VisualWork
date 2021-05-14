package sample;

public class DataModel {

    private String key,name,country,category;
    private int year;
    private double value;

    public DataModel() {
    }

    public DataModel(String key, String name, String country, String category, int year, double value) {
        this.key = key;
        this.name = name;
        this.country = country;
        this.category = category;
        this.year = year;
        this.value = value;
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

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return "DataModel{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", country='" + country + '\'' +
                ", category='" + category + '\'' +
                ", year=" + year +
                ", value=" + value +
                '}';
    }
}
