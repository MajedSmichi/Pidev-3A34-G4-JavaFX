package Controller;

public class UserSession {
    private String verificationCode;
    private String recipientEmail;

    public UserSession(String recipientEmail, String verificationCode) {
        this.recipientEmail = recipientEmail;
        this.verificationCode = verificationCode;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }
}

