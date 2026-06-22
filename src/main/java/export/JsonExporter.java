package export;

import com.fasterxml.jackson.databind.ObjectMapper;
import weather.WeatherReport;

import java.io.File;

public class JsonExporter implements Exporter {
    @Override
    public void export(WeatherReport report, String filePath) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), report);
    }
}
