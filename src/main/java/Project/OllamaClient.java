package Project;

import okhttp3.*;
import org.json.JSONObject;
import java.io.IOException;

public class OllamaClient {
    private static final String OLLAMA_API_URL = "http://localhost:11434/api/generate";
    private static final OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .readTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .writeTimeout(300, java.util.concurrent.TimeUnit.SECONDS)
            .build();

    public static void main(String[] args) {
        String prompt = "who is critiano ronaldo?";
        String response = sendPromptToOllama(prompt);
        System.out.println("Ollama Response: " + response);
    }

    public static String sendPromptToOllama(String prompt) {
        try {
            // ساختن JSON برای درخواست
            JSONObject json = new JSONObject();
            json.put("model", "llama3.1");
            json.put("prompt", prompt);
            json.put("stream", false);

            // ارسال درخواست به Ollama
            RequestBody body = RequestBody.create(json.toString(), MediaType.get("application/json"));
            Request request = new Request.Builder().url(OLLAMA_API_URL).post(body).build();
            Response response = client.newCall(request).execute();

            // بررسی و پردازش پاسخ
            if (response.isSuccessful() && response.body() != null) {
                JSONObject jsonResponse = new JSONObject(response.body().string());
                return jsonResponse.getString("response");
            } else {
                return "Error: " + response.code() + " " + response.message();
            }
        } catch (IOException e) {
            return "Exception: " + e.getMessage();
        }
    }
}
