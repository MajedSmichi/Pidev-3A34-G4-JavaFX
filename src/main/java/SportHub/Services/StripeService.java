package SportHub.Services;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeService {
    private static final String API_KEY = "";

    public StripeService() {
        Stripe.apiKey = API_KEY;
    }

    public String createPaymentIntent(int amount) throws StripeException {
        PaymentIntentCreateParams params = PaymentIntentCreateParams.builder()
                .setCurrency("usd")
                .setAmount((long) amount * 100) // amount in cents
                .build();

        PaymentIntent paymentIntent = PaymentIntent.create(params);
        return paymentIntent.getClientSecret();
    }
}