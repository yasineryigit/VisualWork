package sample;

public class Bar implements Comparable<Bar>{

    private String name, category;
    private int value;

    public Bar(String name, String category, int value) {
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

    @Override
    public int compareTo(Bar o) {//2 barı değerlerine göre karşılaştırır
        return 0;
    }


}
