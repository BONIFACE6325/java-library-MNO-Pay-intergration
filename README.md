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

## Configuration

Create a `.env` file in the root of your project:

```env
# LipaHub Configuration
LIPAHUB_ENV=production
LIPAHUB_API_KEY=your_live_api_key_here
LIPAHUB_API_SECRET=your_live_api_secret_here
```

Ensure your `.env` file is excluded from your version control (`.gitignore`).

## Usage

### 1. Initialize the SDK

You can use the excellent `dotenv-java` library (e.g. `io.github.cdimascio:dotenv-java`) to load `.env` variables safely into your Java app.

```java
import com.lipahub.TzPay;
import com.lipahub.models.PaymentRequest;
import com.lipahub.models.PaymentResponse;
import com.lipahub.config.TzPayConfig;
import io.github.cdimascio.dotenv.Dotenv;

public class Main {
    public static void main(String[] args) {
        // Load .env file
        Dotenv dotenv = Dotenv.load();

        // Initialize SDK in Production mode
        TzPayConfig config = new TzPayConfig();
        config.setEnvironment(dotenv.get("LIPAHUB_ENV", "sandbox"));
        config.setApiKey(dotenv.get("LIPAHUB_API_KEY"));
        config.setApiSecret(dotenv.get("LIPAHUB_API_SECRET"));
        
        TzPay tzpay = new TzPay(config);

        // Create Payment Request
        PaymentRequest req = new PaymentRequest();
        req.setPhone("255714542241"); // Customer phone number
        req.setAmount(1000);          // Amount in TZS
        req.setReference("ORDER-001"); // Your unique order reference

        // Execute Payment (USSD Push)
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

To check if the user has approved the PIN on their phone:

```java
PaymentResponse status = tzpay.queryStatus("ORDER-001");
System.out.println("Status: " + status.getStatus()); // SUCCESS, PENDING, or FAILED
```
