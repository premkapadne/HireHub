package com.hirehub.company.service.impl;

import com.hirehub.company.service.CompanyService;
import com.hirehub.dto.CompanyDto;
import com.hirehub.entity.Company;
import com.hirehub.repository.CompanyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompanyServiceImpl implements CompanyService
{
    private final CompanyRepository companyRepository;

    @Override
    public List<CompanyDto> getAllCompanies()
    {
        List<Company> companyList = companyRepository.findAll();
        return companyList.stream().map(this::transformCompanyToDto).collect(Collectors.toList());
    }

    private CompanyDto transformCompanyToDto(Company company) {
        return new CompanyDto(company.getId(), company.getName(), company.getLogo(),
                company.getIndustry(), company.getSize(), company.getRating(),
                company.getLocations(), company.getFounded(), company.getDescription(),
                company.getEmployees(), company.getWebsite(),company.getCreatedAt());
    }
}
