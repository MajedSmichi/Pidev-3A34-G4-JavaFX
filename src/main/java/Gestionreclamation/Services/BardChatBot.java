package Gestionreclamation.Services;

/*import com.pkslow.ai.AIClient;
import com.pkslow.ai.GoogleBardClient;
import com.pkslow.ai.domain.Answer;*/
import okhttp3.OkHttpClient;

import java.util.concurrent.TimeUnit;

public class BardChatBot {
    private static final String TOKEN = "g.a000jAgeakL-FhlSrw9UWqsZc_oaCwBgfR86MJw2-WTyR0QIRsSR3jmCRHpmTl8DMUJhuOD4fwACgYKAQASAQASFQHGX2MiRWZL1DgUowyu3mZWMMeDwhoVAUF8yKqYIpkWc4MeuavK9sGJpyEt0076;sidts-CjIBLwcBXEVjQ7RKf2Bdz1MGFtMc80qSGe732sVHPnzFkerWVHTKm65w0NHQTTEYhNq8chAA";
    private final OkHttpClient httpClient;

    public BardChatBot() {
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .retryOnConnectionFailure(false)
            .build();
    }

/*
    public String chat() {
        AIClient client = new GoogleBardClient("gg.a000jAgeakL-FhlSrw9UWqsZc_oaCwBgfR86MJw2-WTyR0QIRsSR3jmCRHpmTl8DMUJhuOD4fwACgYKAQASAQASFQHGX2MiRWZL1DgUowyu3mZWMMeDwhoVAUF8yKqYIpkWc4MeuavK9sGJpyEt0076;sidts-CjIBLwcBXIefmOZQLbS0zhyFf9cE3d44Qlx1kguNl9WzwcplCf4AqE7ZiGroR_Sf_R7ITBAA", this.httpClient);
        Answer answer = client.ask("can you show me a picture of clock?");
        if (answer.getChosenAnswer() != null) {
            return answer.getChosenAnswer().toString();
        } else {
            return "No answer available";
        }
    }
*/


}