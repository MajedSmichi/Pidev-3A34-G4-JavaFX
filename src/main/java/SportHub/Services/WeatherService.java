package SportHub.Services;

import kong.unirest.HttpResponse;
import kong.unirest.JsonNode;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

public class WeatherService {

    private static final String BASE_URL = "vv";
    private static final String API_KEY = "vv"; // Replace with your actual API key

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