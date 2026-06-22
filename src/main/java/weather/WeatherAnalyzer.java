package weather;

import kafka.model.Root;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class WeatherAnalyzer {
    private final List<Root> data;

    public WeatherAnalyzer(List<Root> data) {
        this.data = data;
    }

    public double getAverageTemperature() {
        double avgTemp = data.stream()
                .flatMap(root -> root.getSensordatavalues().stream())
                .filter(dataValue -> dataValue.getValue_type().equals("temperature"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .average()
                .orElse(0.0);
        return avgTemp;
    }

    public double getAverageHumidity() {
        double avgHum = data.stream()
                .flatMap(root -> root.getSensordatavalues().stream())
                .filter(dataValue -> dataValue.getValue_type().equals("humidity"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .average()
                .orElse(0.0);
        return avgHum;
    }

    private double getStationTemperature(Root root) {
        double stationTemp = root.getSensordatavalues().stream()
                .filter(value -> value.getValue_type().equals("temperature"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .findFirst()
                .orElse(-1000.0);
        return stationTemp;
    }

    private double getStationHumidity(Root root) {
        double stationHum = root.getSensordatavalues().stream()
                .filter(value -> value.getValue_type().equals("humidity"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .findFirst()
                .orElse(-1000.0);
        return stationHum;
    }

    private double getStationPM10(Root root) {
        double stationPM10 = root.getSensordatavalues().stream()
                .filter(value -> value.getValue_type().equals("P1"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .findFirst()
                .orElse(-1000.0);
        return stationPM10;
    }

    private double getStationPM25(Root root) {
        double stationPM25 = root.getSensordatavalues().stream()
                .filter(value -> value.getValue_type().equals("P2"))
                .map(kafka.model.SensorDataValue::getValue)
                .mapToDouble(Double::parseDouble)
                .findFirst()
                .orElse(-1000.0);
        return stationPM25;
    }

    public Map<Boolean, List<Root>> getStationsAboveAverageTemperature() {
        double avgTemp = getAverageTemperature();

        Map<Boolean, List<Root>> stationsAboveAvegTemp = data.stream()
                .filter(root -> root.getSensordatavalues().stream()
                        .anyMatch(value -> value.getValue_type().equals("temperature")))
                .collect(Collectors.partitioningBy(root -> getStationTemperature(root) > avgTemp));
        return stationsAboveAvegTemp;
    }

    public Map<Boolean, List<Root>> getStationsAboveAverageHumidity() {
        double avgHum = getAverageHumidity();

        Map<Boolean, List<Root>> stationsAboveAvgHum = data.stream()
                .filter(root -> root.getSensordatavalues().stream()
                        .anyMatch(value -> value.getValue_type().equals("humidity")))
                .collect(Collectors.partitioningBy(root -> getStationHumidity(root) > avgHum));
        return stationsAboveAvgHum;
    }

    public Map<Boolean, List<Root>> getStationsBelowAirNorm() {
        final double PM10 = 50.0;
        final double PM25 = 15.0;

        Map<Boolean, List<Root>> stationsBelowAirNorm = data.stream()
                .filter(root -> root.getSensordatavalues().stream()
                        .anyMatch(value -> value.getValue_type().equals("P1") || value.getValue_type().equals("P2")))
                .collect(Collectors.partitioningBy(root -> getStationPM10(root) > PM10 ||  getStationPM25(root) > PM25));
        return stationsBelowAirNorm;
    }
}
