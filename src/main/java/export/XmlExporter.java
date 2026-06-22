package export;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import weather.WeatherReport;

import java.io.File;

public class XmlExporter implements Exporter {
    @Override
    public void export(WeatherReport report, String filePath) throws Exception {
        XmlMapper xmlMapper = new XmlMapper();
        xmlMapper.writerWithDefaultPrettyPrinter().writeValue(new File(filePath), report);
    }
}
