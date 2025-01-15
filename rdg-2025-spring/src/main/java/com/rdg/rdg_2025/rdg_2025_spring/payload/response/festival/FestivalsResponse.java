package com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;

import java.util.ArrayList;

public class FestivalsResponse {

    private ArrayList<Festival> festivals;

    public FestivalsResponse(ArrayList<Festival> festivals) {
        this.festivals = festivals;
    }

    public ArrayList<Festival> getFestivals() {
        return festivals;
    }

    public void setFestivals(ArrayList<Festival> festivals) {
        this.festivals = festivals;
    }
}
