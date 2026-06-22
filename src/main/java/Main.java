import http.DataFetcher;
import kafka.model.Root;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {
        DataFetcher df = new DataFetcher();

        try {
            ArrayList<Root> data = df.fetchData();
            System.out.println(data.size());
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
}
