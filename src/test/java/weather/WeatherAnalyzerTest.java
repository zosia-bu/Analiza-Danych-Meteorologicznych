package weather;

import kafka.model.Root;
import kafka.model.SensorDataValue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

public class WeatherAnalyzerTest {
    private WeatherAnalyzer analyzer;
    private List<Root> mockData;

    private Root createMockStation(String valueType, String value) {
        SensorDataValue sensor = new SensorDataValue();
        sensor.setValue_type(valueType);
        sensor.setValue(value);

        java.util.ArrayList<SensorDataValue> sensorList = new java.util.ArrayList<>();
        sensorList.add(sensor);

        Root root = new Root();
        root.setSensordatavalues(sensorList);

        return root;
    }

    @BeforeEach
    void setUp() {
        Root station1 = createMockStation("temperature", "10.0");
        Root station2 = createMockStation("temperature", "20.0");
        Root station3 = createMockStation("humidity", "50.0");
        Root station4 = createMockStation("P1", "60.0");
        Root station5 = createMockStation("P1", "20.0");

        mockData = Arrays.asList(station1, station2, station3, station4, station5);

        analyzer = new WeatherAnalyzer(mockData);
    }

    @Test
    void testGetAverageTemperature() {
        double avgTemp = analyzer.getAverageTemperature();

        assertEquals(15.0, avgTemp, 0.001, "Srednia temperatura powinna wynosic 15.0");
    }

    @Test
    void testGetAverageHumidity() {
        double avgHum = analyzer.getAverageHumidity();

        assertEquals(50.0, avgHum, 0.001, "Srednia wilgotnosc powinna wynosic 50.0");
    }

    @Test
    void testGetStationsAboveAverageTemperature() {
        Map<Boolean, List<Root>> result = analyzer.getStationsAboveAverageTemperature();

        assertEquals(1, result.get(true).size());
        assertEquals("20.0", result.get(true).get(0).getSensordatavalues().get(0).getValue());

        assertEquals(1, result.get(false).size());
    }

    @Test
    void testGetStationsBelowAirNorm() {
        Map<Boolean, List<Root>> result = analyzer.getStationsBelowAirNorm();

        assertEquals(1, result.get(true).size());

        assertEquals(1, result.get(false).size());
    }

    @Test
    void testAverageTemperatureWrongData() {
        Root corruptedStation = createMockStation("temperature", "brak_danych_z_czujnika");
        java.util.ArrayList<Root> badData = new java.util.ArrayList<>();
        badData.add(corruptedStation);

        WeatherAnalyzer badAnalyzer = new WeatherAnalyzer(badData);

        assertThrows(NumberFormatException.class, () -> {
            badAnalyzer.getAverageTemperature();
        }, "Program powinien zglosic blad formatu liczby dla uszkodzonych danych");
    }
}
