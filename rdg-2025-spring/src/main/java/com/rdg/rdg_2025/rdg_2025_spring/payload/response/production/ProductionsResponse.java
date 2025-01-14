package com.rdg.rdg_2025.rdg_2025_spring.payload.response.production;

import com.rdg.rdg_2025.rdg_2025_spring.models.Production;

import java.util.ArrayList;
import java.util.List;

public class ProductionsResponse {

    private ArrayList<Production> productions;

    public ProductionsResponse(ArrayList<Production> productions) {
        this.productions = productions;
    }

    public ArrayList<Production> getProductions() {
        return productions;
    }

    public void setProductions(ArrayList<Production> productions) {
        this.productions = productions;
    }
}
