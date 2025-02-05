package Project;

import java.io.*;
import java.net.*;
import org.json.*;

public class ConnectAI {

    // این متد از پرامپت دریافت شده استفاده کرده و آن را به مدل LLM ارسال می‌کند.
    public String getModelResponse(String prompt) {
        String apiResponse = sendRequestToAPI(prompt);
        return apiResponse;
    }

    // ارسال درخواست به API
    private String sendRequestToAPI(String prompt) {
        // تنظیم بدنه درخواست به همراه پرامپت
        String requestBody = "{\n" +
                "  \"messages\": [\n" +
                "    {\n" +
                "      \"role\": \"user\",\n" +
                "      \"content\": \"" + prompt + "\"\n" +
                "    }\n" +
                "  ],\n" +
                "  \"model\": \"llama-3.3-70b-versatile\"\n" +
                "}";

        try {
            // تنظیم URL API
            URL url = new URL("https://api.groq.com/openai/v1/chat/completions");
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type", "application/json");
            connection.setRequestProperty("Authorization", "Bearer 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");
            connection.setDoOutput(true);

            // ارسال داده‌ها
            try (OutputStream os = connection.getOutputStream()) {
                byte[] input = requestBody.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            // دریافت پاسخ از API
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream(), "utf-8"));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // استخراج محتوای پاسخ JSON
            JSONObject responseJson = new JSONObject(response.toString());
            String modelResponse = responseJson.getJSONArray("choices")
                    .getJSONObject(0)
                    .getJSONObject("message")
                    .getString("content");

            return modelResponse;
        } catch (IOException | JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
