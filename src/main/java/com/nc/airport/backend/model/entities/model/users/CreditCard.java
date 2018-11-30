package com.nc.airport.backend.model.entities.model.users;

public class CreditCard {

    private int id;
    private String number;
    private String month;
    private String year;
    private String cvv;
    private String nickname;
    private int authorizedUserId;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getMonth() {
        return month;
    }

    public void setMonth(String month) {
        this.month = month;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getAuthorizedUserId() {
        return authorizedUserId;
    }

    public void setAuthorizedUserId(int authorizedUserId) {
        this.authorizedUserId = authorizedUserId;
    }
}
