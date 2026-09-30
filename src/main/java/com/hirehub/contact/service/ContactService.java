package com.hirehub.contact.service;

import com.hirehub.dto.ContactRequestDto;
import com.hirehub.dto.ContactResponseDto;
import org.springframework.data.domain.Page;

import java.util.List;

public interface ContactService
{
    boolean saveContact(ContactRequestDto contactRequestDto);

    // List<ContactResponseDto> fetchNewContactMsgs();

    // List<ContactResponseDto> fetchNewContactMsgsWithSort(String sortBy, String sortDir);

    Page<ContactResponseDto> fetchNewContactMsgsWithPaginationAndSort(int pageNumber, int pageSize,
                                                                      String sortBy, String sortDir);

    boolean closeContactMsg(Long id, String status);
}
