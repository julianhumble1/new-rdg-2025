package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.fasterxml.jackson.annotation.*;
import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import jakarta.persistence.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="venues")
public class Venue {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    private String notes;
    private String postcode;
    private String address;
    private String town;
    private String url;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    @Column(nullable = false, unique = true)
    private String slug;

    @OneToMany(mappedBy = "venue", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Production> productions = new ArrayList<>();

    @OneToMany(mappedBy= "venue", cascade = CascadeType.PERSIST)
    @JsonBackReference
    private List<Festival> festivals = new ArrayList<>();

    public Venue(String name, String notes, String postcode, String address, String town, String url) {
        this.name = name;
        this.notes = notes;
        this.postcode = postcode;
        this.address = address;
        this.town = town;
        this.url = url;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(name);
    }

    public Venue() {
    }

    @Override
    public String toString() {
        return "Venue{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", notes='" + notes + '\'' +
                ", postcode='" + postcode + '\'' +
                ", address='" + address + '\'' +
                ", town='" + town + '\'' +
                ", url='" + url + '\'' +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", slug='" + slug + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public List<Production> getProductions() {
        return productions;
    }

    public void setProductions(List<Production> productions) {
        this.productions = productions;
    }

    public List<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(List<Festival> festivals) {
        this.festivals = festivals;
    }
}
