import http.SensorDataFetcher;
import export.Exporter;
import export.JsonExporter;
import export.PdfExporter;
import export.XmlExporter;
import kafka.model.Root;
import weather.WeatherAnalyzer;
import weather.WeatherReport;

import java.util.List;
import java.util.Map;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        SensorDataFetcher df = new SensorDataFetcher();
        try {
            List<Root> data = df.fetchData();

            WeatherAnalyzer weatherAnalyzer = new WeatherAnalyzer(data);

            System.out.println("Statystyki:\n");
            System.out.printf("Srednia temperatura: %.2f °C\n", weatherAnalyzer.getAverageTemperature());
            System.out.printf("Srednia wilgotnosc: %.2f %%\n", weatherAnalyzer.getAverageHumidity());

            Map<Boolean, List<Root>> tempMap = weatherAnalyzer.getStationsAboveAverageTemperature();
            Map<Boolean, List<Root>> humMap = weatherAnalyzer.getStationsAboveAverageHumidity();
            Map<Boolean, List<Root>> smogMap = weatherAnalyzer.getStationsBelowAirNorm();

            System.out.println("Liczba stacji z temperatura powyzej sredniej: " + tempMap.get(true).size());
            System.out.println("Liczba stacji z wilgotnoscia powyzej sredniej: " + humMap.get(true).size());
            System.out.println("Liczba stacji z przekroczonymi normami smogu: " + smogMap.get(true).size());

            System.out.print("Zapisz jako: P-Pdf J-Json X-xml >> ");

            Scanner scanner = new Scanner(System.in);
            String choice = scanner.nextLine().trim().toUpperCase();

            WeatherReport report = new WeatherReport(
                    weatherAnalyzer.getAverageTemperature(),
                    weatherAnalyzer.getAverageHumidity(),
                    tempMap.get(true).size(),
                    humMap.get(true).size(),
                    smogMap.get(true).size()
            );

            switch (choice) {
                case "P":
                    Exporter pdfExporter = new PdfExporter();
                    pdfExporter.export(report, "statystyki.pdf");
                    System.out.println("Zapisano wynik do pliku pdf");
                    break;
                case "J":
                    Exporter jsonExporter = new JsonExporter();
                    jsonExporter.export(report, "statystyki.json");
                    System.out.println("Zapisano wynik do pliku json");
                    break;
                case "X":
                    Exporter xmlExporter = new XmlExporter();
                    xmlExporter.export(report, "statystyki.xml");
                    System.out.println("Zapisano wynik do pliku xml");
                    break;
                default:
                    System.out.println("Nie rozpoznano formatu zapisu.");
                    break;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
