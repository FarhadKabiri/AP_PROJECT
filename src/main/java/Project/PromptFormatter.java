package Project;

import org.json.JSONObject;

public class PromptFormatter {
    public static String formatPrompt(String question, String context) {

        String message = question + " also the knowledge we have: " + context;

        return message.toString();
    }
}
