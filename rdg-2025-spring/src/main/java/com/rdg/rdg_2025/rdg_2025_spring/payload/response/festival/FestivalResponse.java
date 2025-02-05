package com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;
import com.rdg.rdg_2025.rdg_2025_spring.models.Performance;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter @Setter
public class FestivalResponse {

    private Festival festival;

    private List<Performance> performances;

    public FestivalResponse(Festival festival) {
        this.festival = festival;
        this.performances = festival.getPerformances();
    }
}
