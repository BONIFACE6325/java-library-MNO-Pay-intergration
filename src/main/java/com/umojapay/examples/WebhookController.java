package com.umojapay.examples;

import com.umojapay.LipaHub;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/umojapay")
public class WebhookController {

    private final LipaHub umoja = new LipaHub("YOUR_KEY", "YOUR_SECRET", "sandbox");

    @PostMapping("/callback")
    public String handleWebhook(@RequestBody Map<String, Object> payload, 
                               @RequestHeader("X-Signature") String signature) {
        
        try {
            // Verify if the request truly came from LipaHub
            if (umoja.verifyWebhook(payload, signature)) {
                System.out.println("Payment Update Received: " + payload.get("reference"));
                return "{\"status\": \"success\"}";
            } else {
                return "{\"status\": \"invalid_signature\"}";
            }
        } catch (Exception e) {
            return "{\"status\": \"error\", \"message\": \"" + e.getMessage() + "\"}";
        }
    }
}
