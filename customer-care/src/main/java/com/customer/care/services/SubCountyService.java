package com.customer.care.services;

import com.customer.care.entities.SubCounty;
import com.customer.care.entities.Ward;
import com.customer.care.repositories.SubCountyRepository;
import com.customer.care.repositories.WardRepository;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class SubCountyService {

    private final SubCountyRepository subCountyRepository;
    private final WardRepository wardRepository;

    public SubCountyService(SubCountyRepository subCountyRepository, WardRepository wardRepository) {
        this.subCountyRepository = subCountyRepository;
        this.wardRepository = wardRepository;
    }

    public SubCounty createSubCounty(SubCounty subCounty) {
        return subCountyRepository.save(subCounty);
    }

    public Ward createWard(Ward ward) {
        return wardRepository.save(ward);
    }

    public List<SubCounty> getAllSubCounties() {
        return subCountyRepository.findAll();
    }

    public List<Ward> getAllWardsBySubcountyId(Long subCountyId) {
        SubCounty subCounty=subCountyRepository.findById(subCountyId).get();
        return  wardRepository.findAllBySubCounty(subCounty);
    }
}
