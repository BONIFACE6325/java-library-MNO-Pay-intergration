package com.umojapay;

import okhttp3.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.TreeMap;

public class LipaHub {
    private final String apiKey;
    private final String apiSecret;
    private final String baseUrl;
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();

    public LipaHub(String apiKey, String apiSecret, String environment) {
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
        this.baseUrl = environment.equals("sandbox") 
            ? "http://127.0.0.1:4000/api" 
            : "https://api.umojapay.com/v1";
    }

    private String generateSignature(Map<String, Object> payload) throws Exception {
        // Sort keys alphabetically using TreeMap
        TreeMap<String, Object> sortedPayload = new TreeMap<>(payload);
        String dataString = objectMapper.writeValueAsString(sortedPayload);

        Mac sha256_HMAC = Mac.getInstance("HmacSHA256");
        SecretKeySpec secret_key = new SecretKeySpec(apiSecret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
        sha256_HMAC.init(secret_key);

        byte[] hash = sha256_HMAC.doFinal(dataString.getBytes(StandardCharsets.UTF_8));
        
        StringBuilder hexString = new StringBuilder();
        for (byte b : hash) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) hexString.append('0');
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public String requestPayment(String phone, double amount, String reference) throws Exception {
        Map<String, Object> payload = new TreeMap<>();
        payload.put("phone", phone);
        payload.put("amount", amount);
        payload.put("currency", "TZS");
        payload.put("reference", reference != null ? reference : "REF-" + System.currentTimeMillis());
        payload.put("origin", "Java SDK");

        String signature = generateSignature(payload);
        String jsonPayload = objectMapper.writeValueAsString(payload);

        RequestBody body = RequestBody.create(
            jsonPayload, 
            MediaType.get("application/json; charset=utf-8")
        );

        Request request = new Request.Builder()
            .url(baseUrl + "/payment")
            .post(body)
            .addHeader("Authorization", "Bearer " + apiKey)
            .addHeader("X-API-Secret", apiSecret)
            .addHeader("X-Signature", signature)
            .build();

        try (Response response = client.newCall(request).execute()) {
            if (response.body() == null) return "{}";
            return response.body().string();
        }
    }

    /**
     * Verifies the authenticity of an incoming webhook from LipaHub.
     * @param payload The raw body received in the webhook.
     * @param signature The signature provided in the 'X-Signature' header.
     * @return true if the signature is valid, false otherwise.
     */
    public boolean verifyWebhook(Map<String, Object> payload, String signature) {
        if (signature == null) return false;
        try {
            String expectedSignature = generateSignature(payload);
            return expectedSignature.equals(signature);
        } catch (Exception e) {
            return false;
        }
    }
}
