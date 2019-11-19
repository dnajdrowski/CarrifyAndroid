package pl.carrifyandroid.API.ApiModels;

public class JwtVerifyTokenRequest {

    private String accessToken;

    public JwtVerifyTokenRequest(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
