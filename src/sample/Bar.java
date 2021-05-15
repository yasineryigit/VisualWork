package sample;

public class Bar implements Comparable<Bar>{

    private String key, name, category, country;
    private int value;
    private int year;

    public Bar() {
    }

    public Bar(String key, String name, String category, String country, int value, int year) {
        this.key = key;
        this.name = name;
        this.category = category;
        this.country = country;
        this.value = value;
        this.year = year;
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

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }



    @Override
    public int compareTo(Bar o) {//2 barı değerlerine göre karşılaştırır
        return 0;
    }

    @Override
    public String toString() {
        return "Bar{" +
                "key='" + key + '\'' +
                ", name='" + name + '\'' +
                ", category='" + category + '\'' +
                ", country='" + country + '\'' +
                ", value=" + value +
                ", year=" + year +
                '}';
    }
}
