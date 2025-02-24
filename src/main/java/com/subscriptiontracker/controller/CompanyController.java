package com.subscriptiontracker.controller;

import com.subscriptiontracker.model.Company;
import com.subscriptiontracker.service.CompanyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/companies")
public class CompanyController {
    private final CompanyService companyService;

    @GetMapping("/search")
    public ResponseEntity<List<Company>> searchCompanies(@RequestParam String keyword) {
        List<Company> results = companyService.searchCompanies(keyword);
        return ResponseEntity.ok(results);
    }
}
