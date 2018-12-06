package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;

@ObjectType(ID = "16")
public class CreditCard {

    @ValueField(ID = "51")
    private String number;

    @ValueField(ID = "52")
    private String month;

    @ValueField(ID = "53")
    private String year;

    @ValueField(ID = "54")
    private String cvv;

    @ValueField(ID = "55")
    private String nickname;

    @ReferenceField(ID = "56")
    private int authorizedUserId;

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
