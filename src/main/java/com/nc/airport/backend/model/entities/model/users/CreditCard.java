package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@ObjectType(ID = "16")
public class CreditCard {

    @ValueField(ID = "50")
    private String number;

    @ValueField(ID = "51")
    private String month;

    @ValueField(ID = "52")
    private String year;

    @ValueField(ID = "53")
    private String cvv;

    @ValueField(ID = "54")
    private String nickname;

    @ReferenceField(ID = "55")
    private int authorizedUserId;

//    public String getNumber() {
//        return number;
//    }
//
//    public void setNumber(String number) {
//        this.number = number;
//    }
//
//    public String getMonth() {
//        return month;
//    }
//
//    public void setMonth(String month) {
//        this.month = month;
//    }
//
//    public String getYear() {
//        return year;
//    }
//
//    public void setYear(String year) {
//        this.year = year;
//    }
//
//    public String getCvv() {
//        return cvv;
//    }
//
//    public void setCvv(String cvv) {
//        this.cvv = cvv;
//    }
//
//    public String getNickname() {
//        return nickname;
//    }
//
//    public void setNickname(String nickname) {
//        this.nickname = nickname;
//    }
//
//    public int getAuthorizedUserId() {
//        return authorizedUserId;
//    }
//
//    public void setAuthorizedUserId(int authorizedUserId) {
//        this.authorizedUserId = authorizedUserId;
//    }
}
