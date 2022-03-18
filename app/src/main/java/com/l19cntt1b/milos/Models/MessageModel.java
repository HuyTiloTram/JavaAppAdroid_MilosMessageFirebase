package com.l19cntt1b.milos.Models;

public class MessageModel {
    String uId,message,rId,url;
    int feelingsad,feelingwow,feelinglove,feelinglaugh,feelingangry,feelinglike,totalfeeling;
    Long timestamp;


    public MessageModel(String uId, String message, String rId, Long timestamp) {
        this.uId = uId;
        this.message = message;
        this.rId = rId;
        this.timestamp = timestamp;
    }

    public String getrId() {
        return rId;
    }

    public void setrId(String rId) {
        this.rId = rId;
    }

    public MessageModel(String uId, String message) {
        this.uId = uId;
        this.message = message;
    }

    public MessageModel(){}

    public String getuId() {
        return uId;
    }

    public void setuId(String uId) {
        this.uId = uId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public int getFeelingsad() {
        return feelingsad;
    }

    public void setFeelingsad(int feelingsad) {
        this.feelingsad = feelingsad;
    }

    public int getFeelingwow() {
        return feelingwow;
    }

    public void setFeelingwow(int feelingwow) {
        this.feelingwow = feelingwow;
    }

    public int getFeelinglove() {
        return feelinglove;
    }

    public void setFeelinglove(int feelinglove) {
        this.feelinglove = feelinglove;
    }

    public int getFeelinglaugh() {
        return feelinglaugh;
    }

    public void setFeelinglaugh(int feelinglaugh) {
        this.feelinglaugh = feelinglaugh;
    }

    public int getFeelingangry() {
        return feelingangry;
    }

    public void setFeelingangry(int feelingangry) {
        this.feelingangry = feelingangry;
    }

    public int getFeelinglike() {
        return feelinglike;
    }

    public void setFeelinglike(int feelinglike) {
        this.feelinglike = feelinglike;
    }

    public int getTotalfeeling() {
        return totalfeeling;
    }

    public void setTotalfeeling(int totalfeeling) {
        this.totalfeeling = totalfeeling;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

}
