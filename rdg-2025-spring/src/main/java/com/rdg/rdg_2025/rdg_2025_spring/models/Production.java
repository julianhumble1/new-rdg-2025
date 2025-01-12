package com.rdg.rdg_2025.rdg_2025_spring.models;

import com.rdg.rdg_2025.rdg_2025_spring.helpers.SlugUtils;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name="productions")
public class Production {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String name;

    @ManyToOne
    private Venue venue;

    private String author;
    private String description;
    private LocalDateTime auditionDate;
    private boolean sundowners;
    private boolean notConfirmed;
    private String flyerFile;

    @Column(nullable = false)
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private String slug;

    public Production() {
    }

    public Production(String name, Venue venue, String author, String description, LocalDateTime auditionDate, boolean sundowners, boolean notConfirmed, String flyerFile) {
        this.name = name;
        this.venue = venue;
        this.author = author;
        this.description = description;
        this.auditionDate = auditionDate;
        this.sundowners = sundowners;
        this.notConfirmed = notConfirmed;
        this.flyerFile = flyerFile;

        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();

        this.slug = SlugUtils.generateSlug(name);
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

    public Venue getVenue() {
        return venue;
    }

    public void setVenue(Venue venue) {
        this.venue = venue;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDateTime getAuditionDate() {
        return auditionDate;
    }

    public void setAuditionDate(LocalDateTime auditionDate) {
        this.auditionDate = auditionDate;
    }

    public boolean isSundowners() {
        return sundowners;
    }

    public void setSundowners(boolean sundowners) {
        this.sundowners = sundowners;
    }

    public boolean isNotConfirmed() {
        return notConfirmed;
    }

    public void setNotConfirmed(boolean notConfirmed) {
        this.notConfirmed = notConfirmed;
    }

    public String getFlyerFile() {
        return flyerFile;
    }

    public void setFlyerFile(String flyerFile) {
        this.flyerFile = flyerFile;
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
}
