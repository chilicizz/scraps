public class StructuredConcurrencyTest {

    public static void main(final String[] args) {
        var weather = Weather.readWeather();
        System.out.println("weather = " + weather);
    }
}
