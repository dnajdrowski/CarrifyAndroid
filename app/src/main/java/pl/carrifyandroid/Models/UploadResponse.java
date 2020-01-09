package pl.carrifyandroid.Models;

public class UploadResponse {

    private String body;
    private String statusCode;
    private int statusCodeValue;

    public UploadResponse(String body, String statusCode, int statusCodeValue) {
        this.body = body;
        this.statusCode = statusCode;
        this.statusCodeValue = statusCodeValue;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(String statusCode) {
        this.statusCode = statusCode;
    }

    public int getStatusCodeValue() {
        return statusCodeValue;
    }

    public void setStatusCodeValue(int statusCodeValue) {
        this.statusCodeValue = statusCodeValue;
    }
}
