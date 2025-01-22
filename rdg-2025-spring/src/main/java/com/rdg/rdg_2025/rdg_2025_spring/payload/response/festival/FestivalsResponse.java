package com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;

@Getter @Setter
public class FestivalsResponse {

    private ArrayList<Festival> festivals;

    public FestivalsResponse(ArrayList<Festival> festivals) {
        this.festivals = festivals;
    }
}
