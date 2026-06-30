package com.umojapay.examples;

import com.umojapay.LipaHub;

public class TestPayment {
    public static void main(String[] args) {
        LipaHub umoja = new LipaHub(
            "YOUR_API_KEY",
            "YOUR_API_SECRET",
            "sandbox"
        );

        System.out.println("--- Starting LipaHub Payment Test (Java) ---");

        try {
            String response = umoja.requestPayment(
                "255754123456",
                5000.0,
                "JAVA-TEST-001"
            );
            System.out.println("Response: " + response);
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
