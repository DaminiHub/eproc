package nic.epsdd.biddermanagement.service.impl;

import lombok.extern.slf4j.Slf4j;
import nic.epsdd.biddermanagement.dto.BidderEnrollmentDto;
import nic.epsdd.biddermanagement.entity.GepCorporateTenderer;
import nic.epsdd.biddermanagement.entity.GepCountryMaster;
import nic.epsdd.biddermanagement.entity.GepUser;
import nic.epsdd.biddermanagement.repository.GepCorporateTendererRepository;
import nic.epsdd.biddermanagement.repository.GepCountryMasterRepository;
import nic.epsdd.biddermanagement.repository.GepUserRepository;
import nic.epsdd.biddermanagement.service.BidderEnrollmentService;
import nic.epsdd.biddermanagement.util.DateTimeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

@Service
@Slf4j
public class BidderEnrollmentServiceImpl implements BidderEnrollmentService {
    // Master Tables
    private final GepUserRepository gepUserRepository;
    private final GepCorporateTendererRepository gepCorporateTendererRepository;
    private final GepCountryMasterRepository gepCountryMasterRepository;

    @Autowired
    public BidderEnrollmentServiceImpl(GepUserRepository gepUserRepository,
                                       GepCorporateTendererRepository gepCorporateTendererRepository,
                                       GepCountryMasterRepository gepCountryMasterRepository) {
        this.gepUserRepository = gepUserRepository;
        this.gepCorporateTendererRepository = gepCorporateTendererRepository;
        this.gepCountryMasterRepository=gepCountryMasterRepository;
    }

    @Transactional
    @Override
    public BidderEnrollmentDto registerBidderEnrollment(BidderEnrollmentDto bidderEnrollmentDto) {
        // Create and save GepUser
        Optional<GepCountryMaster> gepCountryMaster=gepCountryMasterRepository.findById(bidderEnrollmentDto.getMobileIsdCodeId());
        GepUser gepUser = new GepUser();
        gepUser.setId(generateUniqueId());
        gepUser.setLoginId(bidderEnrollmentDto.getLoginId());
        gepUser.setPassword("encrypted-password"); // Replace with proper password encryption and handling
        gepUser.setUserType("Bidder");
        gepUser.setCreatedDate(LocalDateTime.now());
        gepUser.setDisplayName(bidderEnrollmentDto.getContactName());
        gepUser.setMobileNo(bidderEnrollmentDto.getMobileNumber());
        gepUser.setAlternateEmailId(bidderEnrollmentDto.getCorrespondenceEmail());
        gepUser.setMobileIsdCodeId(bidderEnrollmentDto.getMobileIsdCodeId());
        gepUser.setMobileIsdCode(gepCountryMaster.get().getPhoneIsdCode());
        gepUser.setUserStatus("Active");

        // Save GepUser and auto-generate the id
        gepUser = gepUserRepository.save(gepUser);

        // Create and save GepCorporateTenderer
        GepCorporateTenderer gepCorporateTenderer = new GepCorporateTenderer();
        gepCorporateTenderer.setId(generateUniqueId());
        gepCorporateTenderer.setUserId(gepUser.getId()); // Link the saved GepUser's ID
        gepCorporateTenderer.setCompany(bidderEnrollmentDto.getCompanyName());
        gepCorporateTenderer.setRegisteredAddress(bidderEnrollmentDto.getRegisteredAddress());
        gepCorporateTenderer.setCorporateAddress("Null");
        gepCorporateTenderer.setEstablishedYear(bidderEnrollmentDto.getEstablishmentYear());
        gepCorporateTenderer.setBusinessNature(bidderEnrollmentDto.getNatureOfBusiness());
        gepCorporateTenderer.setLegalStatus(bidderEnrollmentDto.getLegalStatus());
        gepCorporateTenderer.setTitle(bidderEnrollmentDto.getTitle());
        gepCorporateTenderer.setContactName(bidderEnrollmentDto.getContactName());
        gepCorporateTenderer.setDesignation(bidderEnrollmentDto.getDesignation());
        gepCorporateTenderer.setPhoneIsdCodeId(bidderEnrollmentDto.getPhoneIsdCodeId());
        gepCorporateTenderer.setPhoneStdCode(bidderEnrollmentDto.getPhoneStdCode());
        gepCorporateTenderer.setPhone(bidderEnrollmentDto.getPhoneNumber());
        gepCorporateTenderer.setBidderUniqueId(bidderEnrollmentDto.getBidderUniqueId());

        // Set bidder type-specific fields based on bidderType (1 for Indian, 2 for Foreign)
        Integer bidderType = bidderEnrollmentDto.getBidderType();
        if (bidderType == 1) {  // Indian Bidder
            gepCorporateTenderer.setPanNumber(bidderEnrollmentDto.getPanNumber());
            gepCorporateTenderer.setStateId(bidderEnrollmentDto.getStateId());
            gepCorporateTenderer.setCountryId(null);
        } else if (bidderType == 2) {  // Foreign Bidder
            gepCorporateTenderer.setCountryId(bidderEnrollmentDto.getCountryId());
            gepCorporateTenderer.setPanNumber(null);
            gepCorporateTenderer.setStateId(null);
        } else {
            throw new IllegalArgumentException("Bidder type must be either '1' (Indian) or '2' (Foreign). Provided value: " + bidderType);
        }

        gepCorporateTenderer.setDateOfBirth(bidderEnrollmentDto.getDateOfBirth());
        gepCorporateTenderer.setHintQuestion("Null");
        gepCorporateTenderer.setHintAnswer("Null");
        gepCorporateTenderer.setCorporateClass(null);
        gepCorporateTenderer.setPrpOption(null);
        gepCorporateTenderer.setPupOption(null);
        gepCorporateTenderer.setBidderCategory(bidderEnrollmentDto.getCompanyCategory());
        gepCorporateTenderer.setRegNumber(bidderEnrollmentDto.getRegistrationNumber());
        gepCorporateTenderer.setCity(bidderEnrollmentDto.getCity());
        gepCorporateTenderer.setPostalCode(bidderEnrollmentDto.getPostalCode());
        gepCorporateTenderer.setSelfUpdatedDate(null);

        // Check if preferential bidder is true
        if (Boolean.TRUE.equals(bidderEnrollmentDto.getPreferentialBidder())) {
            gepCorporateTenderer.setIsPrivilegedBidder(true);

            // Check if preference category ID is provided
            if (bidderEnrollmentDto.getPreferenceCategoryId() == null) {
                throw new IllegalArgumentException("Preference category must be selected if preferential bidder is true.");
            }

            gepCorporateTenderer.setPrivilegeMasterId(bidderEnrollmentDto.getPreferenceCategoryId());
        } else {
            // If preferentialBidder is false, set default values
            gepCorporateTenderer.setIsPrivilegedBidder(false);
            gepCorporateTenderer.setPrivilegeMasterId(null);
        }

        //here we can throw error msg too
        gepCorporateTenderer.setMobileIsdCode(gepCountryMaster.get().getPhoneIsdCode());

        // Save GepCorporateTenderer
        gepCorporateTendererRepository.save(gepCorporateTenderer);

        log.info("Bidder enrollment registered successfully for userId: {}", gepUser.getId());
        return bidderEnrollmentDto;
    }


    private Long generateUniqueId() {
        Long uniqueId = 0L;
        boolean unique = false;

        while (!unique) {
            // Generate a random 5-digit number
            uniqueId = ThreadLocalRandom.current().nextLong(10000, 100000);

            // Check if the ID already exists in the database
            if (isUniqueId(uniqueId)) {
                unique = true;
            }
        }

        return uniqueId;
    }

    private boolean isUniqueId(Long id) {
        // Implement this method to check if the ID already exists in the database
        return gepUserRepository.findById(id).isEmpty();
    }
}
