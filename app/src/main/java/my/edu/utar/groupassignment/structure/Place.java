package my.edu.utar.groupassignment.structure;

import java.io.Serializable;

public class Place implements Serializable {
    private String name;
    private String rating;
    private String photo;
    private String address;
    private String price_level;
    private String description;
    private String web_url;

    public Place(String name, String rating, String photo, String address, String price_level, String description, String web_url)
    {
        this.name = name;
        this.rating = rating;
        this.photo = photo;
        this.address = address;
        this.price_level = price_level;
        this.description = description;
        this.web_url = web_url;
    }

    public String getWeb_url() {
        return web_url;
    }

    public void setWeb_url(String web_url) {
        this.web_url = web_url;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPrice_level() {
        return price_level;
    }

    public void setPrice_level(String price_level) {
        this.price_level = price_level;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
