package com.example.paybill;

public class User {

    private int id;
    private String firstname;
    private String lastname;
    private String phone;
    private String email;
    private String address;
    private String genre;

    public User(int id,String lastname,String firstname,String phone,String email,String address,String genre) {
        this.id = id;
        this.setLastname(lastname);
        this.setFirstname(firstname);
        this.setPhone(phone);
        this.setEmail(email);
        this.setAddress(address);
        this.setGenre(genre);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", firstname='" + firstname + '\'' +
                ", lastname='" + lastname + '\'' +
                ", phone='" + phone + '\'' +
                ", email='" + email + '\'' +
                ", address='" + address + '\'' +
                ", genre='" + genre + '\'' +
                '}';
    }
}