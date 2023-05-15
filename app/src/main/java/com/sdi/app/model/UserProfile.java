package com.sdi.app.model;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Past;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profiles")
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Min(value = 1, message = "Bio must be between 1 and 200")
    @Max(value = 200, message = "Bio must be between 1 and 200")
    private String bio;

    @Column
    @Min(value = 1, message = "Location must be between 1 and 50")
    @Max(value = 50, message = "Location must be between 1 and 50")
    private String location;

    @Column
    @Past(message = "Date of Birth must be in the past")
    private Date birthdate;

    @Column
    private String gender;

    @Column
    private String maritalStatus;
}
