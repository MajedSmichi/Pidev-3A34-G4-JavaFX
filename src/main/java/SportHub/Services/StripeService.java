package SportHub.Services;
import com.stripe.Stripe;
import com.stripe.exception.StripeException;
import com.stripe.model.PaymentIntent;
import com.stripe.param.PaymentIntentCreateParams;

public class StripeService {
    private static final String API_KEY = "sk_test_51OqH27C0U7VRZI3sxyJ6WbBq1E9tZfo6AyoFcgXJcFLt7WNQliTkR9q55XsecR1RZ14k7ptgTqASkHpitVdIUVif00n7lLJLCN";

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