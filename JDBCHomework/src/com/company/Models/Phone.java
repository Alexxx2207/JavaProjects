package com.company.Models;

public class Phone {

    private int id;
    private int personId;
    private String tel;


    public Phone(String tel) {
        this.tel = tel;
    }

    public Phone(int id, String tel) {
        this.tel = tel;
        this.id = id;
    }

    public Phone(String tel, int personId) {
        this.tel = tel;
        this.personId = personId;
    }


    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}
