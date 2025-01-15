package com.rdg.rdg_2025.rdg_2025_spring.payload.response.festival;

import com.rdg.rdg_2025.rdg_2025_spring.models.Festival;

public class FestivalResponse {

    private Festival festival;

    public FestivalResponse(Festival festival) {
        this.festival = festival;
    }

    public Festival getFestival() {
        return festival;
    }

    public void setFestival(Festival festival) {
        this.festival = festival;
    }
}
