package nic.epsdd.biddermanagement.service;

import jakarta.validation.Valid;
import nic.epsdd.biddermanagement.dto.BidderEnrollmentDto;

public interface BidderEnrollmentService {
    BidderEnrollmentDto registerBidderEnrollment(@Valid BidderEnrollmentDto bidderEnrollmentDto);
}
