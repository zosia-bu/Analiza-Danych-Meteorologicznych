package weather;

public class WeatherReport {
    private final double avgTemp;
    private final double avgHum;
    private final int stationsAboveAvgTemp;
    private final int stationsAboveAvgHum;
    private final int stationsAboveAirNorms;

    public WeatherReport(double avgTemp, double avgHum, int stationsAboveAvgTemp, int stationsAboveAvgHum, int stationsAboveAirNorms) {
        this.avgTemp = avgTemp;
        this.avgHum = avgHum;
        this.stationsAboveAvgTemp = stationsAboveAvgTemp;
        this.stationsAboveAvgHum = stationsAboveAvgHum;
        this.stationsAboveAirNorms = stationsAboveAirNorms;
    }

    public double getAvgTemp() {return avgTemp;}
    public double getAvgHum() {return avgHum;}
    public int getStationsAboveAvgTemp() {return stationsAboveAvgTemp;}
    public int getStationsAboveAvgHum() {return stationsAboveAvgHum;}
    public int getStationsAboveAirNorms() {return stationsAboveAirNorms;}
}
