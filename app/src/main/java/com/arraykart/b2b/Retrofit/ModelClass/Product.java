
package com.arraykart.b2b.Retrofit.ModelClass;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

//implements Serializable because to pass data of type Prouct,
//it must be serialized
public class Product implements Serializable {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("technical_name")
    @Expose
    private String technicalName;
    @SerializedName("category")
    @Expose
    private String category;
    @SerializedName("price")
    @Expose
    private String price;
    @SerializedName("volume")
    @Expose
    private String volume;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("target_disease")
    @Expose
    private String targetDisease;
    @SerializedName("target_field_crops")
    @Expose
    private String targetFieldCrops;
    @SerializedName("target_vegetable_crops")
    @Expose
    private String targetVegetableCrops;
    @SerializedName("target_fruit_crops")
    @Expose
    private String targetFruitCrops;
    @SerializedName("target_plantation_crops")
    @Expose
    private String targetPlantationCrops;
    @SerializedName("mode_of_action")
    @Expose
    private String modeOfAction;
    @SerializedName("duration_of_effect")
    @Expose
    private String durationOfEffect;
    @SerializedName("compatability_with_other_chemicals")
    @Expose
    private String compatabilityWithOtherChemicals;
    @SerializedName("frequency_of_application")
    @Expose
    private String frequencyOfApplication;
    @SerializedName("dosage")
    @Expose
    private String dosage;
    @SerializedName("water_requirement (ltr)")
    @Expose
    private String waterRequirementLtr;
    @SerializedName("time_of_application")
    @Expose
    private String timeOfApplication;
    @SerializedName("method_of_application")
    @Expose
    private String methodOfApplication;
    @SerializedName("waiting_period")
    @Expose
    private String waitingPeriod;
    @SerializedName("phytotoxicity")
    @Expose
    private String phytotoxicity;
    @SerializedName("storage")
    @Expose
    private String storage;
    @SerializedName("country_of_origin")
    @Expose
    private String countryOfOrigin;
    @SerializedName("dimensions")
    @Expose
    private String dimensions;
    @SerializedName("state(solid/liquid)")
    @Expose
    private String stateSolidLiquid;
    @SerializedName("inventory_id")
    @Expose
    private Integer inventoryId;
    @SerializedName("brand")
    @Expose
    private String brand;
    @SerializedName("sku")
    @Expose
    private String sku;
    @SerializedName("created_at")
    @Expose
    private String createdAt;
    @SerializedName("modified_at")
    @Expose
    private String modifiedAt;
    @SerializedName("deleted_at")
    @Expose
    private String deletedAt;
    @SerializedName("image")
    @Expose
    private String image;
    @SerializedName("sowing_time")
    @Expose
    private String sowingTime;
    @SerializedName("seed_Rate(Kg/acre)")
    @Expose
    private String seedRateKgAcre;
    @SerializedName("maturity_duration")
    @Expose
    private String maturityDuration;
    @SerializedName("colour")
    @Expose
    private String colour;
    @SerializedName("usp")
    @Expose
    private String usp;
    @SerializedName("Number of Seeds/Packet")
    @Expose
    private String numberOfSeedsPacket;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalName() {
        return technicalName;
    }

    public void setTechnicalName(String technicalName) {
        this.technicalName = technicalName;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTargetDisease() {
        return targetDisease;
    }

    public void setTargetDisease(String targetDisease) {
        this.targetDisease = targetDisease;
    }

    public String getTargetFieldCrops() {
        return targetFieldCrops;
    }

    public void setTargetFieldCrops(String targetFieldCrops) {
        this.targetFieldCrops = targetFieldCrops;
    }

    public String getTargetVegetableCrops() {
        return targetVegetableCrops;
    }

    public void setTargetVegetableCrops(String targetVegetableCrops) {
        this.targetVegetableCrops = targetVegetableCrops;
    }

    public String getTargetFruitCrops() {
        return targetFruitCrops;
    }

    public void setTargetFruitCrops(String targetFruitCrops) {
        this.targetFruitCrops = targetFruitCrops;
    }

    public String getTargetPlantationCrops() {
        return targetPlantationCrops;
    }

    public void setTargetPlantationCrops(String targetPlantationCrops) {
        this.targetPlantationCrops = targetPlantationCrops;
    }

    public String getModeOfAction() {
        return modeOfAction;
    }

    public void setModeOfAction(String modeOfAction) {
        this.modeOfAction = modeOfAction;
    }

    public String getDurationOfEffect() {
        return durationOfEffect;
    }

    public void setDurationOfEffect(String durationOfEffect) {
        this.durationOfEffect = durationOfEffect;
    }

    public String getCompatabilityWithOtherChemicals() {
        return compatabilityWithOtherChemicals;
    }

    public void setCompatabilityWithOtherChemicals(String compatabilityWithOtherChemicals) {
        this.compatabilityWithOtherChemicals = compatabilityWithOtherChemicals;
    }

    public String getFrequencyOfApplication() {
        return frequencyOfApplication;
    }

    public void setFrequencyOfApplication(String frequencyOfApplication) {
        this.frequencyOfApplication = frequencyOfApplication;
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }

    public String getWaterRequirementLtr() {
        return waterRequirementLtr;
    }

    public void setWaterRequirementLtr(String waterRequirementLtr) {
        this.waterRequirementLtr = waterRequirementLtr;
    }

    public String getTimeOfApplication() {
        return timeOfApplication;
    }

    public void setTimeOfApplication(String timeOfApplication) {
        this.timeOfApplication = timeOfApplication;
    }

    public String getMethodOfApplication() {
        return methodOfApplication;
    }

    public void setMethodOfApplication(String methodOfApplication) {
        this.methodOfApplication = methodOfApplication;
    }

    public String getWaitingPeriod() {
        return waitingPeriod;
    }

    public void setWaitingPeriod(String waitingPeriod) {
        this.waitingPeriod = waitingPeriod;
    }

    public String getPhytotoxicity() {
        return phytotoxicity;
    }

    public void setPhytotoxicity(String phytotoxicity) {
        this.phytotoxicity = phytotoxicity;
    }

    public String getStorage() {
        return storage;
    }

    public void setStorage(String storage) {
        this.storage = storage;
    }

    public String getCountryOfOrigin() {
        return countryOfOrigin;
    }

    public void setCountryOfOrigin(String countryOfOrigin) {
        this.countryOfOrigin = countryOfOrigin;
    }

    public String getDimensions() {
        return dimensions;
    }

    public void setDimensions(String dimensions) {
        this.dimensions = dimensions;
    }

    public String getStateSolidLiquid() {
        return stateSolidLiquid;
    }

    public void setStateSolidLiquid(String stateSolidLiquid) {
        this.stateSolidLiquid = stateSolidLiquid;
    }

    public Integer getInventoryId() {
        return inventoryId;
    }

    public void setInventoryId(Integer inventoryId) {
        this.inventoryId = inventoryId;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(String deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSowingTime() {
        return sowingTime;
    }

    public void setSowingTime(String sowingTime) {
        this.sowingTime = sowingTime;
    }

    public String getSeedRateKgAcre() {
        return seedRateKgAcre;
    }

    public void setSeedRateKgAcre(String seedRateKgAcre) {
        this.seedRateKgAcre = seedRateKgAcre;
    }

    public String getMaturityDuration() {
        return maturityDuration;
    }

    public void setMaturityDuration(String maturityDuration) {
        this.maturityDuration = maturityDuration;
    }

    public String getColour() {
        return colour;
    }

    public void setColour(String colour) {
        this.colour = colour;
    }

    public String getUsp() {
        return usp;
    }

    public void setUsp(String usp) {
        this.usp = usp;
    }

    public String getNumberOfSeedsPacket() {
        return numberOfSeedsPacket;
    }

    public void setNumberOfSeedsPacket(String numberOfSeedsPacket) {
        this.numberOfSeedsPacket = numberOfSeedsPacket;
    }

}
