package SportHub.Services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class WeatherService {

    private static final String BASE_URL = "http://api.openweathermap.org/data/2.5/weather";
    private static final String API_KEY = "100139112fc443f511bf72a8c6f8024f"; // Replace with your actual API key

    public JSONObject getWeatherByCity(String city) {
        HttpResponse<JsonNode> response = Unirest.get(BASE_URL)
                .queryString("q", city)
                .queryString("appid", API_KEY)
                .asJson();

        if (response.getStatus() == 200) {
            return response.getBody().getObject();
        } else {
            return null;
        }
    }
}