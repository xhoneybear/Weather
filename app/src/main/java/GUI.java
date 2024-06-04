import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Background;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class GUI extends Scene {

    public static VBox root = new VBox();

    public ImageView icon = new ImageView();
    public Text city = new Text("----"),
                update = new Text("--:--"),
                temp = new Text("--°C"),
                feels_like = new Text("--°C"),
                wind = new Text("-- m/s"),
                humidity = new Text("--%"),
                pressure = new Text("-- hPa"),
                visibility = new Text("-- m"),
                sunrise = new Text("--:--"),
                sunset = new Text("--:--");
        TextField input = new TextField();
        VBox results = new VBox();

    public GUI() {
        super(root, 300, 300);
        this.getStylesheets().add("style.css");

        root.setBackground(Background.fill(Color.valueOf("#000010")));
        root.setAlignment(Pos.TOP_CENTER);
        root.setPadding(new Insets(16));
        root.setSpacing(8);

        input.setVisible(false);
        StackPane text = new StackPane(city, input, results);
        ImageView edit = new ImageView(new Image("edit.png"));
        ImageView key = new ImageView(new Image("key.png"));
        HBox top = new HBox(text, edit, key);
        top.setAlignment(Pos.CENTER);
        top.setSpacing(8);
        edit.setOnMouseClicked(eh -> {
                input.setOnKeyPressed(k -> {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    input.setVisible(false);
                    Logic.setCity(input.getText());
                    input.clear();
                }
            });
            input.setVisible(true);
        });
        key.setOnMouseClicked(eh -> {
                input.setOnKeyPressed(k -> {
                if (k.getCode().equals(KeyCode.ENTER)) {
                    input.setVisible(false);
                    Logic.setKey(input.getText());
                    input.clear();
                }
            });
            input.setVisible(true);
        });

        HBox up = new HBox(new Text("Last update: "), update);

        HBox mid = new HBox(icon, temp);

        HBox feels = new HBox(new Text("Feels like: "), feels_like);

        HBox sun = new HBox(
            new VBox(new Text("Sunrise"), sunrise),
            new VBox(new Text("Sunset"), sunset)
        );
        sun.setSpacing(32);

        HBox extra = new HBox(
            new VBox(new Text("Wind"), wind),
            new VBox(new Text("Humidity"), humidity),
            new VBox(new Text("Pressure"), pressure),
            new VBox(new Text("Visibility"), visibility)
        );
        extra.setSpacing(16);

        VBox forecast = new VBox();

        Text footer = new Text("Made with ❤️ by lisek.dev");

        root.getChildren().addAll(top, up, mid, feels, sun, extra, forecast, footer);
    }

    public void update(WeatherData weather) {
            icon.setImage(new Image("https://openweathermap.org/img/wn/%s.png".formatted(weather.weather[0].icon)));
            city.setText("%s, %s".formatted(weather.name, weather.sys.country));
            update.setText("%d:%2d".formatted((weather.dt + weather.timezone) % 86400 / 3600, (weather.dt + weather.timezone) % 86400 % 3600 / 60).replace(' ', '0'));
            temp.setText("%s°C".formatted(weather.main.temp));
            feels_like.setText("%s°C".formatted(weather.main.feels_like));
            wind.setText("%s m/s".formatted(weather.wind.speed));
            humidity.setText("%s%%".formatted(weather.main.humidity));
            pressure.setText("%s hPa".formatted(weather.main.pressure));
            visibility.setText("%s m".formatted(weather.visibility));
            sunrise.setText("%d:%2d".formatted((weather.sys.sunrise + weather.timezone) % 86400 / 3600, (weather.sys.sunrise + weather.timezone) % 86400 % 3600 / 60).replace(' ', '0'));
            sunset.setText("%d:%2d".formatted((weather.sys.sunset + weather.timezone) % 86400 / 3600, (weather.sys.sunset + weather.timezone) % 86400 % 3600 / 60).replace(' ', '0'));
    }
}
