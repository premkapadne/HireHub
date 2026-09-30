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
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contacts")
@RequiredArgsConstructor
public class ContactController {

    private final ContactService contactService;

    @PostMapping(path = "/public",version = "1.0")
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
