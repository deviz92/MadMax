package com.polito.mad17.madmax.entities;

import java.util.HashMap;
import java.util.Map;

public class Expense {
    private String ID;

    private String description;
    private String category;        // optional, corrisponde al categoryID della categoria a cui corrisponde la spesa
    private Double amount;
    private String currency;        // €, $ ...
    private String image;           // optional, URL dell'immagine su Firebase (può essere lo scontrino o una foto del prodotto)
    private String billPhoto;
    private String groupID;
    private String creatorID;       //ID of the user who added the expense
    private Boolean equallyDivided; // se vero la spesa viene divisa equamente fra tutti gli utenti del gruppo
                                    // altrimenti viene suddivisa come specificato in participants ->
    private HashMap<String, Double> participants;     // String: userID, Double: frazione corrispondente a quello user

    public Expense() {}

    public Expense (String ID, String description, String category, Double amount, String currency, String billPhoto, String image, Boolean equallyDivided, String groupID, String creatorID) {
        this.ID = ID;
        this.description = description;
        this.category = category;
        this.amount = amount;
        this.currency = currency;
        this.billPhoto = billPhoto;
        this.image = image;
        this.equallyDivided = equallyDivided;
        this.participants = new HashMap<>();
        this.groupID = groupID;
        this.creatorID = creatorID;
    }


    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String name) {
        this.description = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Boolean getEquallyDivided() {
        return equallyDivided;
    }

    public void setEquallyDivided(Boolean equallyDivided) {
        this.equallyDivided = equallyDivided;
    }

    public HashMap<String, Double> getParticipants() {
        return participants;
    }

    public void setParticipants(HashMap<String, Double> participants) {
        this.participants = participants;
    }

    public String getGroupID() {return groupID;}

    public void setGroupID(String groupID) {this.groupID = groupID;}

    public String getBillPhoto() {return billPhoto;}

    public void setBillPhoto(String billPhoto) {this.billPhoto = billPhoto;}

    public String getCreatorID() {return creatorID;}

    public void setCreatorID(String creatorID) {this.creatorID = creatorID;}


    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("ID", ID);
        result.put("description", description);
        result.put("category", category);
        result.put("amount", amount);
        result.put ("currency", currency);
        result.put("image", image);
        result.put("equallyDivided", equallyDivided);
        result.put("groupID", groupID);

        return result;
    }
}
