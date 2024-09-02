package com.scm.smartContactManager.forms;

import org.springframework.web.multipart.MultipartFile;

import com.scm.smartContactManager.validators.ValidFile;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ContactForm {

    @NotBlank(message = "Name is required")
    private String name;
    @NotBlank(message = "Email is required")
    private String email;
    @NotBlank(message = "Phone Number is required")
    @Pattern(regexp = "^[0-9]{10}$", message = "Please enter a valid phone number")
    private String phoneNumber;
    @NotBlank(message = "Address is required")
    private String address;

    @ValidFile(message = "Invalid file")
    private MultipartFile contactImage;
    private String description;
    private boolean favorite = false;
    private String category;
    private String websiteLink;
    private String picture;

}
