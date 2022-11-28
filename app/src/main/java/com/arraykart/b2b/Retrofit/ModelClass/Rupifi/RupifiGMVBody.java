
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;
import com.arraykart.b2b.Retrofit.ModelClass.Rupifi.Body;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RupifiGMVBody {

    @SerializedName("body")
    @Expose
    private Body body;
    @SerializedName("rupifi_access_token")
    @Expose
    private String rupifiAccessToken;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RupifiGMVBody() {
    }

    /**
     * 
     * @param body
     * @param rupifiAccessToken
     */
    public RupifiGMVBody(Body body, String rupifiAccessToken) {
        super();
        this.body = body;
        this.rupifiAccessToken = rupifiAccessToken;
    }

    public Body getBody() {
        return body;
    }

    public void setBody(Body body) {
        this.body = body;
    }

    public String getRupifiAccessToken() {
        return rupifiAccessToken;
    }

    public void setRupifiAccessToken(String rupifiAccessToken) {
        this.rupifiAccessToken = rupifiAccessToken;
    }

}
