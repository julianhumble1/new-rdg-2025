package com.rdg.rdg_2025.rdg_2025_spring.payload.request.venue;

import jakarta.validation.constraints.NotBlank;

public class NewVenueRequest {

    @NotBlank
    private String name;

    private String notes;
    private String postcode;
    private String address;
    private String town;
    private String url;

    public NewVenueRequest() {
    }

    public NewVenueRequest(String name, String notes, String postcode, String address, String town, String url) {
        this.name = name;
        this.notes = notes;
        this.postcode = postcode;
        this.address = address;
        this.town = town;
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public String toString() {
        return "NewVenueRequest{" +
                "name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", postcode='" + postcode + '\'' +
                ", address='" + address + '\'' +
                ", town='" + town + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
