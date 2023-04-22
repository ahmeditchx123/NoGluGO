package com.noglugo.mvp.service.dto;

import java.io.Serializable;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

public class RegisterDTO implements Serializable {

    @NotBlank
    private String firstName;

    @NotBlank
    private String lastName;

    @Email
    @NotBlank
    private String email;

    @Size(min = 8, max = 50)
    private String password;

    @Size(min = 8, max = 8)
    private String phoneNumber;

    @NotBlank
    private String role;

    private StoreDTO storeDTO;

    public RegisterDTO() {}

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public StoreDTO getStoreDTO() {
        return storeDTO;
    }

    public void setStoreDTO(StoreDTO storeDTO) {
        this.storeDTO = storeDTO;
    }
}
