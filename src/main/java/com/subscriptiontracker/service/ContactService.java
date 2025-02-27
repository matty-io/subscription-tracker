package com.subscriptiontracker.service;

import com.subscriptiontracker.DTO.ContactDTO;
import com.subscriptiontracker.model.Contact;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.repository.ContactRepository;
import com.subscriptiontracker.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ContactService {

    @Autowired
    private ContactRepository contactRepository;

    @Autowired
    private UserRepository userRepository;

    public ContactDTO createContact(ContactDTO contactDTO) {
        User user = userRepository.findById(contactDTO.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Contact contact = new Contact();
        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());
        contact.setUser(user);

        Contact savedContact = contactRepository.save(contact);
        return convertToDTO(savedContact);
    }

    public ContactDTO getContact(Long id) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));
        return convertToDTO(contact);
    }

    public List<ContactDTO> getContactsByUserId(Long userId) {
        List<Contact> contacts = contactRepository.findByUserId(userId);
        return contacts.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ContactDTO updateContact(Long id, ContactDTO contactDTO) {
        Contact contact = contactRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Contact not found"));

        contact.setName(contactDTO.getName());
        contact.setEmail(contactDTO.getEmail());

        Contact updatedContact = contactRepository.save(contact);
        return convertToDTO(updatedContact);
    }

    public void deleteContact(Long id) {
        contactRepository.deleteById(id);
    }

    private ContactDTO convertToDTO(Contact contact) {
        return new ContactDTO(
                contact.getId(),
                contact.getName(),
                contact.getEmail(),
                contact.getUser().getId());
    }
}