package Gestionreclamation.Services;
import com.assemblyai.api.AssemblyAI;
import com.assemblyai.api.resources.transcripts.types.*;

import java.io.File;

public class AssemblyAIService {
    AssemblyAI client;
    String audioUrl;
    TranscriptOptionalParams params;
    Transcript transcript;

    public void assembly(){
        client = AssemblyAI.builder()
                .apiKey("58b81cc2566948339ff753017f702bd2" )
                .build();

        String audioUrl = "https://storage.googleapis.com/aai-web-samples/5_common_sports_injuries.mp3";

        params = TranscriptOptionalParams.builder()
                .speakerLabels(true)
                .build();

        Transcript transcript = client.transcripts().transcribe(audioUrl, params);

        System.out.println(transcript.getText());    }
    public void printTranscript() {
        System.out.println(transcript.getText());
        transcript.getUtterances().ifPresent(utterances ->
                utterances.forEach(utterance ->
                        System.out.println("Speaker " + utterance.getSpeaker() + ": " + utterance.getText())
                )
        );
    }
}