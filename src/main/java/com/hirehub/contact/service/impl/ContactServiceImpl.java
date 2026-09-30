package com.hirehub.contact.service.impl;

import com.hirehub.contact.service.ContactService;
import com.hirehub.contants.ApplicationConstants;
import com.hirehub.dto.ContactRequestDto;
import com.hirehub.dto.ContactResponseDto;
import com.hirehub.entity.Contact;
import com.hirehub.repository.ContactRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ContactServiceImpl implements ContactService
{
    private final ContactRepository contactRepository;

    @Override
    public boolean saveContact(ContactRequestDto contactRequestDto)
    {
        boolean result = false;
        Contact contact = contactRepository.save(transformToEntity(contactRequestDto));
        if(contact != null && contact.getId() != null) {
            result = true;
        }
        return result;
    }

//    @Override
//    public List<ContactResponseDto> fetchNewContactMsgs() {
//        List<Contact> contacts = contactRepository.findContactsByStatusOrderByCreatedAtAsc
//                (ApplicationConstants.NEW_MESSAGE);
//        List<ContactResponseDto> responseDtos = contacts.stream()
//                .map(this::transformToDto)
//                .collect(Collectors.toList());
//        return responseDtos;
//    }

//    @Override
//    public List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir) {
//        // Create Sort object based on sortBy and sortDir parameters
//        Sort sort = sortDir.equalsIgnoreCase("desc")
//                ? Sort.by(sortBy).descending()
//                : Sort.by(sortBy).ascending();
//        List<Contact> contacts = contactRepository.findContactsByStatus(
//                ApplicationConstants.NEW_MESSAGE, sort);
//        List<ContactResponseDto> responseDtos = contacts.stream()
//                .map(this::transformToDto)
//                .collect(Collectors.toList());
//        return responseDtos;
//    }

    @Override
    public Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(
            int pageNumber, int pageSize, String sortBy, String sortDir) {
        // Create Sort object based on sortBy and sortDir parameters
        Sort sort = sortDir.equalsIgnoreCase("desc")
                ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        // Create Pageable object with page number, page size, and sorting
        Pageable pageable = PageRequest.of(pageNumber, pageSize, sort);
        // Fetch paginated and sorted contacts from repository
        Page<Contact> contactPage = contactRepository.findContactsByStatus(
                ApplicationConstants.NEW_MESSAGE, pageable);

        // Transform Contact entities to ContactResponseDto
        Page<ContactResponseDto> responseDtoPage = contactPage.map(this::transformToDto);
        return responseDtoPage;
    }

    @Override
    public boolean closeContactMsg(Long id, String status) {
        Contact contact = contactRepository.findById(id).orElse(null);
        if (contact == null) {
            return false;
        } else {
            contact.setStatus(status);
            contactRepository.save(contact);
        }
        return true;
    }

    private Contact transformToEntity(ContactRequestDto contactRequestDto) {
        Contact contact = new Contact();
        BeanUtils.copyProperties(contactRequestDto, contact);
        contact.setCreatedAt(Instant.now());
        contact.setCreatedBy("System");
        contact.setStatus("NEW");
        return contact;
    }

    private ContactResponseDto transformToDto(Contact contact) {
        ContactResponseDto contactResponseDto = new ContactResponseDto(contact.getId(),
                contact.getName(), contact.getEmail(), contact.getUserType(), contact.getSubject(),
                contact.getMessage(), contact.getStatus(), contact.getCreatedAt());
        return contactResponseDto;
    }
}
