package pl.carrifyandroid.API.ApiModels;

public class AuthRequest {

    private String action;
    private String password;
    private String personalNumber;
    private String email;
    private String phone;
    private String userId;
    private String token;

    public AuthRequest(String action, String password, String personalNumber, String email, String phone) {
        this.action = action;
        this.password = password;
        this.personalNumber = personalNumber;
        this.email = email;
        this.phone = phone;
    }

    public AuthRequest(String action, String password, String personalNumber, String email, String phone, String userId, String token) {
        this.action = action;
        this.password = password;
        this.personalNumber = personalNumber;
        this.email = email;
        this.phone = phone;
        this.userId = userId;
        this.token = token;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPersonalNumber() {
        return personalNumber;
    }

    public void setPersonalNumber(String personalNumber) {
        this.personalNumber = personalNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
