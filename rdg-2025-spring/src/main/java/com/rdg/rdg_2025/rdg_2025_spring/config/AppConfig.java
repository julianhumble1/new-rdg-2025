package com.rdg.rdg_2025.rdg_2025_spring.config;

import com.rdg.rdg_2025.rdg_2025_spring.services.ProductionService;
import com.rdg.rdg_2025.rdg_2025_spring.services.VenueService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {

    @Bean
    public VenueService venueService() {
        return new VenueService();
    }

    @Bean
    public ProductionService productionService(VenueService venueService) {
        return new ProductionService(venueService);
    }

}
