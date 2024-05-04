package Gestionreclamation.Services;

import java.util.Arrays;
import java.util.List;

public class BadWordService {

    private List<String> badWords = Arrays.asList("test1", "test2", "test"); // Replace with actual bad words

    public boolean containsBadWords(String input) {
        String[] words = input.toLowerCase().split("\\s+"); // Split the input into words
        for (String word : words) {
            if (badWords.contains(word)) {
                return true;
            }
        }
        return false;
    }
}