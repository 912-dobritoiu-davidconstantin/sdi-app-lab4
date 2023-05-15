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
import org.hibernate.validator.constraints.Length;

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
    @Length(min = 1, max = 200, message = "Bio must be between 1 and 200 characters")
    private String bio;

    @Column
    @Length(min = 1, max = 200, message = "Location must be between 1 and 200 characters")
    private String location;

    @Column
    @Past(message = "Date of Birth must be in the past")
    private Date birthdate;

    @Column
    private String gender;

    @Column
    private String maritalStatus;
}
