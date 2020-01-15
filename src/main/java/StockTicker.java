public class StockTicker {
    String source;
    double value;

    public StockTicker(String source, double value) {
        this.source = source;
        this.value = value;
    }
    public String getSource() {return source;}
    public double getValue() {return value;}

    @Override
    public String toString() {
        return "source:" + source + " value:" + value;
    }
}
