import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class Logic {
    public static Gson gson = new GsonBuilder()
        .setPrettyPrinting()
        .create();
    public static HttpClient client = HttpClient.newHttpClient();
    
    public static Settings initSettings() {
        try {
            return gson.fromJson(Files.readString(Path.of(Logic.class.getResource("/settings.json").getPath())), Settings.class);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (NullPointerException e) {
            try {
                String path = Logic.class.getResource("/style.css").getPath();
                Files.writeString(Paths.get(path.substring(0, path.length() - 9) + "settings.json"), gson.toJson(new Settings()));
            } catch (IOException e1) {
                throw new RuntimeException(e1);
            }
            return initSettings();
        }
    }

    public static void getWeather() {
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create("https://api.openweathermap.org/data/2.5/weather?lat=%f&lon=%f&units=metric&appid=%s".formatted(App.settings.lat, App.settings.lon, App.settings.key)))
            .build();
        client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(res -> App.gui.update(gson.fromJson(res, WeatherData.class)));
    }
    
    public static void setCity(String query) {
        HttpRequest request = HttpRequest
            .newBuilder()
            .uri(URI.create("http://api.openweathermap.org/geo/1.0/direct?q=%s&appid=%s".formatted(query.replace(' ', '+'), App.settings.key)))
            .build();
        client.sendAsync(request, BodyHandlers.ofString())
            .thenApply(HttpResponse::body)
            .thenAccept(res -> {
                City city = gson.fromJson(res, City[].class)[0];
                App.settings.lat = city.lat;
                App.settings.lon = city.lon;
                updateSettings();
                getWeather();
            });
    }

    public static void setKey(String key) {

        App.settings.key = key;
        Logic.updateSettings();
        getWeather();
    }

    public static void updateSettings() {
        String path = Logic.class.getResource("/style.css").getPath();
        try {
            System.out.println(path.substring(0, path.length() - 9));
            Files.writeString(Paths.get(path.substring(0, path.length() - 9) + "settings.json"), gson.toJson(App.settings));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}