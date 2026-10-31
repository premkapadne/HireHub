package com.hirehub.company.controller;

import com.hirehub.company.service.CompanyService;
import com.hirehub.dto.CompanyDto;
import com.hirehub.dto.ErrorResponseDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/companies")
@RequiredArgsConstructor
public class CompanyController {

    private final CompanyService companyService;

    @GetMapping(path = "/public",version = "1.0")
    @Operation(summary = "List public companies", description = "Returns the companies available for public browsing.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Companies returned successfully"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    public ResponseEntity<List<CompanyDto>> getAllCompanies() {
        List<CompanyDto> companyList = companyService.getAllCompanies();
        return ResponseEntity.ok().body(companyList);
    }

    @PostMapping(path = "/admin", version = "1.0")
    @Operation(summary = "Create a company", description = "Creates a company record. Requires the ADMIN role and a valid company request body.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Company created"), @ApiResponse(responseCode = "500", description = "Company creation failed or internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> createCompany(@RequestBody @Valid CompanyDto companyDto) {
        boolean isCreated = companyService.createCompany(companyDto);
        if (isCreated) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }
    }

    @GetMapping(path = "/admin", version = "1.0")
    @Operation(summary = "List companies for administrators", description = "Returns company records for an authenticated administrator.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Companies returned successfully"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<CompanyDto>> getAllCompaniesForAdmin() {
        List<CompanyDto> companyList = companyService.getAllCompaniesForAdmin();
        return ResponseEntity.ok().body(companyList);
    }

    @PutMapping(path = "/{id}/admin", version = "1.0")
    @Operation(summary = "Update company details", description = "Updates the company identified by id using the supplied company details. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company details updated"), @ApiResponse(responseCode = "400", description = "Company details could not be updated or id is invalid"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> updateCompanyDetails(@PathVariable @NotBlank String id,
                                                       @RequestBody @Valid CompanyDto companyDto) {
        boolean isUpdated = companyService.updateCompanyDetails(Long.valueOf(id),companyDto);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Company details updated successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update Company details");
        }
    }

    @DeleteMapping(path = "/{id}/admin", version = "1.0")
    @Operation(summary = "Delete a company", description = "Deletes the company identified by id. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Company deleted"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> deleteCompanyById(@PathVariable @NotBlank String id) {
        companyService.deleteCompanyById(Long.valueOf(id));
        return ResponseEntity.status(HttpStatus.OK).body("Company record deleted successfully.");
    }

}
