package Gestionreclamation.Services;
/*

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONObject;


import java.io.File;
*/

import kong.unirest.HttpResponse;
import kong.unirest.Unirest;
import kong.unirest.json.JSONObject;

import java.io.File;

public class openIAsevice {


    public String executeService(String filePath) {
        String transcription = null;

        try {
            HttpResponse<String> response = Unirest.post("https://api.openai.com/v1/audio/transcriptions")
                    .header("Authorization", "Bearer " + "h")
                    .field("file", new File(filePath))
                    .field("model", "whisper-1")
                    .asString();
            System.out.println("Response Body: " + response.getBody()); // Print the response body

            JSONObject jsonResponse = new JSONObject(response.getBody());
            transcription = jsonResponse.getString("text");
            System.out.println(transcription);
            System.out.println("valid");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return transcription;
    }


}