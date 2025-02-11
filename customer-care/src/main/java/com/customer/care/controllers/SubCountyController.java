package com.customer.care.controllers;

import com.customer.care.entities.Ward;
import com.customer.care.services.SubCountyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/subcounty")
public class SubCountyController {
    @Autowired
    SubCountyService subCountyService;

    @GetMapping("/wards/{subCountyId}")
    public List<Ward> fetchWards(@PathVariable("subCountyId")long subCountyId ){
        return subCountyService.getAllWardsBySubcountyId(subCountyId);
    }
}
