package com.example.auth_v1;

import java.util.Objects;

public class User {
    private String name;
    private String email;
    private String cin;
    private String phone_number;
    private String Adreesse1;
    private String Adresse2;
    private String country;
    private String state;
    private String city;
    private String zipcode;

    public User(String name, String email, String cin, String phone_number) {
        this.name = name;
        this.email = email;
        this.cin = cin;
        this.phone_number = phone_number;
    }

    public User() {

    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(name, user.name) &&
                Objects.equals(email, user.email) &&
                Objects.equals(cin, user.cin) &&
                Objects.equals(phone_number, user.phone_number);
    }

    public String getAdreesse1() {
        return Adreesse1;
    }

    public void setAdreesse1(String adreesse1) {
        Adreesse1 = adreesse1;
    }

    public String getAdresse2() {
        return Adresse2;
    }

    public void setAdresse2(String adresse2) {
        Adresse2 = adresse2;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public void setZipcode(String zipcode) {
        this.zipcode = zipcode;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, cin, phone_number);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCin() {
        return cin;
    }

    public void setCin(String cin) {
        this.cin = cin;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }
}
