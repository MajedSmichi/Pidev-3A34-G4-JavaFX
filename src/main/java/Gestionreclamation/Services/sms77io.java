package Gestionreclamation.Services;



public class sms77io {
    private static final String API_KEY = "bbfcb444camsh4dd7eb503e470e0p10a826jsnab284abc42fc"; // Replace with your API key
        private static final String RAPIDAPI_KEY = "bbfcb444camsh4dd7eb503e470e0p10a826jsnab284abc42fc";

        // ...

/*
        public void sendVoiceSms(String to, String text, String from, String p, int ringtime) {
            try {
                HttpResponse<String> response = Unirest.post("https://sms77io.p.rapidapi.com/voice")
                        .header("content-type", "application/x-www-form-urlencoded")
                        .header("X-RapidAPI-Key", "bbfcb444camsh4dd7eb503e470e0p10a826jsnab284abc42fc) // Here's where the RAPIDAPI_KEY is used")
                        .header("X-RapidAPI-Host", "sms77io.p.rapidapi.com")
                        .body(String.format("to=%s&text=%s&from=%s&p=%s&ringtime=%d", to, text, from, p, ringtime))
                        .asString();

                System.out.println("Response status code: " + response.getStatus());
                System.out.println("Response body: " + response.getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
        }
*/
    }
