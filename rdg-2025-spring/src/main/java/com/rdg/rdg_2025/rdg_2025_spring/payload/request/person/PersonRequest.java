package com.rdg.rdg_2025.rdg_2025_spring.payload.request.person;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @NoArgsConstructor @ToString
public class PersonRequest {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    private String summary;
    private String homePhone;
    private String mobilePhone;
    private String addressStreet;
    private String addressTown;
    private String addressPostcode;
    private String imageId;

    public PersonRequest(String firstName, String lastName, String summary, String homePhone, String mobilePhone, String addressStreet, String addressTown, String addressPostcode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.summary = summary;
        this.homePhone = homePhone;
        this.mobilePhone = mobilePhone;
        this.addressStreet = addressStreet;
        this.addressTown = addressTown;
        this.addressPostcode = addressPostcode;
    }
}
