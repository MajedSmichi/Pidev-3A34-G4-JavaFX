package Gestionreclamation.Services;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

public class GrammarCheck {
    private static final String OPENAI_API_KEY = "your_openai_api_key"; // Replace with your OpenAI API key
    String content = "";

/*
    public String checkBadWords(String text) {
        try {
            HttpResponse<JsonNode> response = Unirest.post("https://api.openai.com/v1/chat/completions")
                    .header("Content-Type", "application/json")
                    .header("Authorization", "Bearer " + "sk-proj-1XWpJ9lFUJXKaftgyl82T3BlbkFJJYZMTHIS2amBu1wTsuAM")
                    .body(new JSONObject()
                            .put("model", "gpt-3.5-turbo")
                            .put("messages", new JSONArray()
                                    .put(new JSONObject()
                                            .put("role", "system")
                                            .put("content", "You are a bot.")
                                    )
                                    .put(new JSONObject()
                                            .put("role", "user")
                                            .put("content", "this text contains badwords" + text)
                                    )
                            )
                    )
                    .asJson();
            System.out.println("Response from OpenAI API: " + response.getBody().toString());
            JSONObject jsonResponse = response.getBody().getObject();
            if (jsonResponse.has("content")) {
                content = jsonResponse.getString("content");
            }
            System.out.println("Content: " + content);
        } catch (UnirestException e) {
            System.out.println("An error occurred while making the request to the OpenAI API:");
            e.printStackTrace();
        }
        return content;
    }
*/
}