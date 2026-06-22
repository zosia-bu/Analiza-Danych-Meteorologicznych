package export;

import weather.WeatherReport;

public interface Exporter {
    void export(WeatherReport report, String filePath) throws Exception;
}
