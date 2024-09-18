package nic.epsdd.biddermanagement.repository;

import nic.epsdd.biddermanagement.entity.GepCountryMaster;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GepCountryMasterRepository extends JpaRepository<GepCountryMaster, Long> {
}