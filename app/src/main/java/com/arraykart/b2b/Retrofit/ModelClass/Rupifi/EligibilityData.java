
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class EligibilityData {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("message")
    @Expose
    private String message;
    @SerializedName("requestId")
    @Expose
    private String requestId;
    @SerializedName("timestamp")
    @Expose
    private String timestamp;
    @SerializedName("data")
    @Expose
    private Data data;

    /**
     * No args constructor for use in serialization
     * 
     */
    public EligibilityData() {
    }

    /**
     * 
     * @param data
     * @param success
     * @param requestId
     * @param message
     * @param timestamp
     */
    public EligibilityData(Boolean success, String message, String requestId, String timestamp, Data data) {
        super();
        this.success = success;
        this.message = message;
        this.requestId = requestId;
        this.timestamp = timestamp;
        this.data = data;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }

}
