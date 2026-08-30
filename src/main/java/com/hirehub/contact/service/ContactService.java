package com.hirehub.contact.service;

import com.hirehub.dto.ContactRequestDto;

public interface ContactService
{
    boolean saveContact(ContactRequestDto contactRequestDto);
}
