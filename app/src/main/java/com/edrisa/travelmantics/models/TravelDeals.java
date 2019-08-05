package com.edrisa.travelmantics.models;

public class TravelDeals {



    private String id;
    private String city;
    private String price;
    private String name;
    private String image_url;

    public TravelDeals(String id, String city, String price, String name, String image_url) {
        this.id = id;
        this.city = city;
        this.price = price;
        this.name = name;
        this.image_url = image_url;
    }

    public TravelDeals(String city, String price, String name) {
        this.city = city;
        this.price = price;
        this.name = name;
    }

    public TravelDeals() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }
}
