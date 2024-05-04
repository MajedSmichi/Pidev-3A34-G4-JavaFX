package Gestionreclamation.Controller;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;

public class WhatsAppSender {
    // Twilio credentials
    public static final String ACCOUNT_SID = "AC0fbbf84256db348c5eb7c9e734b14bcf";
    public static final String AUTH_TOKEN = "6fdeec2b816dc70dd831faafe725ade1";

    public static void main(String[] args) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);

        Message message = Message.creator(
                        new PhoneNumber("whatsapp:"+args[1] ),
                        new PhoneNumber("whatsapp:+14155238886"),
                        "Your verification code is: " + args[0])
                .create();

        System.out.println("WhatsApp message sent successfully.");
    }
}
