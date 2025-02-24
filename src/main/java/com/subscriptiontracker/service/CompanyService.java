package com.subscriptiontracker.service;

import com.subscriptiontracker.model.Company;
import com.subscriptiontracker.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CompanyService {
    private final CompanyRepository companyRepository;

    public List<Company> searchCompanies(String keyword) {
        return companyRepository.searchCompanies(keyword);
    }
}
