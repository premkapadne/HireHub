package com.hirehub.contact.controller;

import com.hirehub.contact.service.ContactService;
import com.hirehub.contants.ApplicationConstants;
import com.hirehub.dto.ContactRequestDto;
import com.hirehub.dto.ContactResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import com.hirehub.dto.ErrorResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping(path = "/public",version = "1.0")
    @Operation(summary = "Submit a contact message", description = "Submits a contact message using the supplied validated contact details.")
    @ApiResponses({@ApiResponse(responseCode = "201", description = "Contact message submitted"), @ApiResponse(responseCode = "500", description = "Message submission failed or internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class)))})
    public ResponseEntity<String> saveContactMsg(@RequestBody @Valid ContactRequestDto contactRequestDto) {
        boolean isSaved =  contactService.saveContact(contactRequestDto);
        if (isSaved) {
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body("Request processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Request processing failed");
        }
    }

//    @GetMapping("/admin")
//    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgs() {
//        List<ContactResponseDto> contactResponseDtos = contactService.fetchNewContactMsgs();
//        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
//    }

//    @GetMapping("/sort/admin")
//    public ResponseEntity<List<ContactResponseDto>> fetchNewContactMsgsWithSort(
//            @RequestParam(defaultValue = "createdAt") String sortBy,
//            @RequestParam(defaultValue = "asc") String sortDir) {
//        List<ContactResponseDto> contactResponseDtos = contactService
//                .fetchNewContactMsgsWithSort(sortBy, sortDir);
//        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtos);
//    }

    @GetMapping("/page/admin")
    @Operation(summary = "List contact messages", description = "Returns a page of contact messages sorted by the requested field and direction. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Contact messages returned"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<ContactResponseDto>> fetchNewContactMsgsWithPaginationAndSort(
            @RequestParam(defaultValue = "0") int pageNumber,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {
        Page<ContactResponseDto> contactResponseDtoPage = contactService
                .fetchNewContactMsgsWithPaginationAndSort(pageNumber, pageSize, sortBy, sortDir);
        return ResponseEntity.status(HttpStatus.OK).body(contactResponseDtoPage);
    }

    @PatchMapping("/{id}/status/admin")
    @Operation(summary = "Close a contact message", description = "Marks the contact message identified by id as closed. Requires the ADMIN role.")
    @ApiResponses({@ApiResponse(responseCode = "200", description = "Contact message updated"), @ApiResponse(responseCode = "400", description = "Contact message could not be updated"), @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema(implementation = ErrorResponseDto.class))), @ApiResponse(responseCode = "401", description = "Authentication required"), @ApiResponse(responseCode = "403", description = "ADMIN role required")})
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> closeContactMsg(@PathVariable String id) {
        boolean isUpdated = contactService.closeContactMsg(Long.valueOf(id),
                ApplicationConstants.CLOSED_MESSAGE);
        if (isUpdated) {
            return ResponseEntity.status(HttpStatus.OK).body("Contact message updated successfully.");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Failed to update contact message.");
        }
    }
}
