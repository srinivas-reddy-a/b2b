
package com.arraykart.b2b.Retrofit.ModelClass.Rupifi;


import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;


public class RupifiEligibilityResponse {

    @SerializedName("success")
    @Expose
    private Boolean success;
    @SerializedName("eligibility_data")
    @Expose
    private EligibilityData eligibilityData;

    /**
     * No args constructor for use in serialization
     * 
     */
    public RupifiEligibilityResponse() {
    }

    /**
     * 
     * @param success
     * @param eligibilityData
     */
    public RupifiEligibilityResponse(Boolean success, EligibilityData eligibilityData) {
        super();
        this.success = success;
        this.eligibilityData = eligibilityData;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public EligibilityData getEligibilityData() {
        return eligibilityData;
    }

    public void setEligibilityData(EligibilityData eligibilityData) {
        this.eligibilityData = eligibilityData;
    }

}
