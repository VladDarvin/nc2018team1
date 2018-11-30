package com.nc.airport.backend.model.entities.model.users;

public class User {

    private int id;
    private String login;
    private String password;
    private String email;
    private String phone;
    private String nickname;
    private int userRoleIdUserRole;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

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
