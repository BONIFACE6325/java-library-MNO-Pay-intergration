# LipaHub Java SDK

The official Java library for LipaHub mobile money integrations in Tanzania.

## Installation

Clone the repository and install it to your local Maven repository:

```bash
git clone https://github.com/BONIFACE6325/java-library-MNO-Pay-intergration.git
cd java-library-MNO-Pay-intergration
./mvnw clean install
```

Then, include the dependency in your project's `pom.xml`:

```xml
<dependency>
    <groupId>com.lipahub</groupId>
    <artifactId>tzpay-sdk</artifactId>
    <version>1.0.0</version>
</dependency>
```

## Usage

### 1. Simple USSD Push (Sandbox)

```java
import com.lipahub.TzPay;
import com.lipahub.models.PaymentRequest;
import com.lipahub.models.PaymentResponse;
import com.lipahub.config.TzPayConfig;

public class Main {
    public static void main(String[] args) {
        // Initialize SDK (Sandbox mode)
        TzPayConfig config = new TzPayConfig();
        config.setEnvironment("sandbox");
        
        TzPay tzpay = new TzPay(config);

        // Create Payment Request
        PaymentRequest req = new PaymentRequest();
        req.setPhone("255714542241");
        req.setAmount(1000);
        req.setReference("ORDER-001");

        // Execute Payment
        try {
            PaymentResponse response = tzpay.requestPayment(req);
            System.out.println("Success! Transaction ID: " + response.getTransactionId());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

### 2. Checking Status

```java
PaymentResponse status = tzpay.queryStatus("ORDER-001");
System.out.println("Status: " + status.getStatus());
```

## Production Usage

Switch to the live ClickPesa gateway by adding credentials to your configuration:

```java
TzPayConfig config = new TzPayConfig();
config.setEnvironment("production");
config.setClickPesaApiKey("YOUR_KEY");
config.setClickPesaApiSecret("YOUR_SECRET");

TzPay tzpay = new TzPay(config);
```
