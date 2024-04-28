package SportHub.Services;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class TwilioService {

    // These are your Twilio Account SID and Auth Token
    private static final String ACCOUNT_SID = "ACcabe995c392f3c6ac59a663e22e7b326";
    private static final String AUTH_TOKEN = "acc309941bff8fe69b3ea7ffca7b7407";

    public void sendSms(String to, String from, String body) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                new PhoneNumber(to),  // Replace with your phone number
                new PhoneNumber(from),  // Replace with your Twilio number
                body)
            .create();

        System.out.println(message.getSid());
    }
}