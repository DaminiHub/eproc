package nic.epsdd.biddermanagement.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.Column;
import jakarta.persistence.PrePersist;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BidderEnrollmentDto {
// 1.

    // * ------------------- Login and Contact Details -------------------
    private Long id;

    @NotBlank(message = "Login ID is required. -> (e.g., abc@xyz.com ).")
    @Pattern(regexp = "^[\\S]+@[\\S]+\\.[\\S]+$", message = "Email must not contain spaces.")
    @Email(message = "Invalid Login ID format. Please follow the format instructions.")
    private String loginId;

    @NotBlank(message = "Please provide active email address.")
    @Pattern(regexp = "^[\\S]+@[\\S]+\\.[\\S]+$", message = "Email must not contain spaces.")
    @Email(message = "Invalid email format. Provide a active email address.")
    private String correspondenceEmail;

    //    @NotBlank(message = "Please select the country's SID code.")
    private Long mobileIsdCodeId;

    private String bidderUniqueId;


    @NotNull(message = "Please provide a active mobile number.")
    @Pattern(regexp = "^\\d{10}$", message = "Mobile number field does not allow Space.")
    @Size(min = 10, max = 10, message = "Mobile number must be exactly 10 digits.")
    private String mobileNumber;

// 2.

    // * ------------------- Company Details -------------------
    @NotBlank(message = "Please provide company name.")
    private String companyName;


    // # ------------------- Preferential Bidder -------------------
    // If preferentialBidder is true, preferenceCategoryId is mandatory.

    private Boolean preferentialBidder;

    // If preferentialBidder is false, preferenceCategoryId should not be set.
    private Long preferenceCategoryId;


    // ------------------- Company Legal Details -------------------
    @NotBlank(message = "Registration number is required.")
    @Size(max = 50, message = "Registration number must be less than or equal to 50 characters.")
    private String registrationNumber;

    @NotBlank(message = "Registered address is required.")
    @Size(max = 100, message = "Registered address must be less than or equal to 100 characters.")
    private String registeredAddress;

    @NotBlank(message = "Information on partners or directors is required.")
    @Size(max = 100, message = "Partners/Directors information must be less than or equal to 100 characters.")
    private String partners_Directors;

    // ------------------- Bidder Type -------------------
    // Type of bidder (1 for Indian, 2 for Foreign).
    @Min(value = 1, message = "Bidder type must be 1 (Indian) or 2 (Foreign).")
    @Max(value = 2, message = "Bidder type must be 1 (Indian) or 2 (Foreign).")
    private Integer bidderType;

    // If Bidder Type is Indian, then stateId and panNumber are mandatory, and countryId must not be set.
    private Long stateId;

    @Size(max = 10, message = "PAN number must be less than or equal to 10 characters.")
    @Pattern(regexp = "[A-Z]{5}[0-9]{4}[A-Z]", message = "Invalid PAN number format. The correct format is ABCDE1234F.")
    private String panNumber;

    // If Bidder Type is Foreign, countryId is mandatory, and stateId and panNumber must not be set.
    private Long countryId;

    // ------------------- Common Fields for Both Indian and Foreign Bidders -------------------
    @NotBlank(message = "City name is required.")
    @Size(max = 100, message = "City name must be less than or equal to 100 characters.")
    private String city;

    @NotBlank(message = "Postal code is required.")
    @Pattern(regexp = "^\\d{6}$", message = "Postal code must be a valid 6-digit number.")
    private String postalCode;

    private Integer establishmentYear;

    @NotBlank(message = "Nature of business is required.")
    @Size(max = 100, message = "Nature of business must be less than or equal to 100 characters.")
    private String natureOfBusiness;

    @NotBlank(message = "Please select the company's legal status.")
    private String legalStatus;

    @NotBlank(message = "Please select your company category.")
    private String companyCategory;

// 3.

    // ------------------- Company’s Contact Person Details -------------------
    @NotBlank(message = "Please select your title.")
    private String title;

    @NotBlank(message = "Contact person's name is required.")
    @Size(max = 100, message = "Contact Name must be less than or equal to 100 characters.")
    private String contactName;


    // User's Date of Birth (DD/MM/YYYY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
    private LocalDate dateOfBirth;

    @NotBlank(message = "Designation is required for the contact person.")
    @Size(max = 50, message = "Designation must be less than or equal to 50 characters.")
    private String designation;

    private Long phoneIsdCodeId;

    @NotBlank(message = "Phone number is required.")
    @Size(min = 2, max = 8, message = "STD Code must be 2 to 8 digits.")
    private String phoneStdCode;

    @NotBlank(message = "Phone number is required.")
    @Pattern(regexp = "^\\d{10}$", message = "Phone number must be a valid 10-digit number.")
    private String phoneNumber;

    // ------------------- Bidder Role -------------------
    @Column(name = "bidder_role", nullable = false)
    private String bidderRole;

    @PrePersist
    private void prePersistBidderRole() {
        if (this.bidderRole == null) {
            this.bidderRole = "Bidder";
        }
    }


}
