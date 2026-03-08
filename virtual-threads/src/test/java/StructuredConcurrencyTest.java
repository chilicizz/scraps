import org.junit.jupiter.api.Test;

public class StructuredConcurrencyTest {

    @Test
    public void runTest() {
            var weather = Weather.readWeather();
            System.out.println("weather = " + weather);
    }
}
