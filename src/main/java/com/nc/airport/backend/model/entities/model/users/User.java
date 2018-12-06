package com.nc.airport.backend.model.entities.model.users;

import com.nc.airport.backend.eav.annotations.ObjectType;
import com.nc.airport.backend.eav.annotations.attribute.value.ReferenceField;
import com.nc.airport.backend.eav.annotations.attribute.value.ValueField;
import com.nc.airport.backend.model.BaseEntity;

@ObjectType(ID = "15")
public class User extends BaseEntity {

    @ValueField(ID = "45")
    private String login;

    @ValueField(ID = "46")
    private String password;

    @ValueField(ID = "47")
    private String email;

    @ValueField(ID = "48")
    private String phone;

    @ValueField(ID = "49")
    private String nickname;

    @ReferenceField(ID = "50")
    private int userRoleIdUserRole;

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getUserRoleIdUserRole() {
        return userRoleIdUserRole;
    }

    public void setUserRoleIdUserRole(int userRoleIdUserRole) {
        this.userRoleIdUserRole = userRoleIdUserRole;
    }
}
