import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {

    public static Settings settings = Logic.initSettings();
    public static GUI gui = new GUI();

    @Override
    public void start(Stage stage) {
        stage.setTitle("Weather");
        stage.setScene(gui);
        stage.show();
        stage.setOnCloseRequest(eh -> System.exit(0));
        Thread thread = new Thread(() -> {
            while (stage.isShowing()) {
                Logic.getWeather();
                try {
                    Thread.sleep(60000);
                } catch (InterruptedException e) {
                }
            }
        });
        thread.start();
    };

    public static void main(String[] args) {
        launch(args);
    }
}
