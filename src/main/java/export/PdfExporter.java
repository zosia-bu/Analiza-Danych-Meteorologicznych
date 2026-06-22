package export;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;
import weather.WeatherReport;

import java.io.FileOutputStream;

public class PdfExporter implements Exporter {
    @Override
    public void export(WeatherReport report, String filePath) throws Exception {
        Document document = new Document();

        PdfWriter.getInstance(document, new FileOutputStream(filePath));

        document.open();

        Font titleFont = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
        Font textFont = FontFactory.getFont(FontFactory.HELVETICA, 12);

        document.add(new Paragraph("Statystyki meteorologiczne - raport", titleFont));
        document.add(new Paragraph(" ", textFont));

        document.add(new Paragraph(String.format("- Srednia temperatura: %.2f C", report.getAvgTemp()), textFont));
        document.add(new Paragraph(String.format("- Srednia wilgotnosc: %.2f %%", report.getAvgHum()), textFont));
        document.add(new Paragraph("- Stacje powyzej sredniej temp: " + report.getStationsAboveAvgTemp(), textFont));
        document.add(new Paragraph("- Stacje powyzej sredniej wilgotnosci: " + report.getStationsAboveAvgHum(), textFont));
        document.add(new Paragraph("- Stacje z przekroczonym smogiem: " + report.getStationsAboveAirNorms(), textFont));

        document.close();
    }
}
