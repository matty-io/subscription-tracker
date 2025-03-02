package com.subscriptiontracker.controller;

import com.subscriptiontracker.DTO.ContactDTO;
import com.subscriptiontracker.model.User;
import com.subscriptiontracker.service.ContactService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/contacts")
public class ContactController {

    @Autowired
    private ContactService contactService;

    @PostMapping
    public ResponseEntity<ContactDTO> createContact(@AuthenticationPrincipal User user, @RequestBody ContactDTO contactDTO) {
        contactDTO.setUserId(user.getId());
        return ResponseEntity.ok(contactService.createContact(contactDTO));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ContactDTO> getContact(@AuthenticationPrincipal User user, @PathVariable Long id) {
        ContactDTO contact = contactService.getContact(id);
        if (!contact.getUserId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contact);
    }

    @GetMapping
    public ResponseEntity<List<ContactDTO>> getContactsByUser(@AuthenticationPrincipal User user) {
        return ResponseEntity.ok(contactService.getContactsByUserId(user.getId()));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ContactDTO> updateContact(@AuthenticationPrincipal User user, @PathVariable Long id, @RequestBody ContactDTO contactDTO) {
        ContactDTO existingContact = contactService.getContact(id);
        if (!existingContact.getUserId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(contactService.updateContact(id, contactDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@AuthenticationPrincipal User user, @PathVariable Long id) {
        ContactDTO contact = contactService.getContact(id);
        if (!contact.getUserId().equals(user.getId())) {
            return ResponseEntity.notFound().build();
        }
        contactService.deleteContact(id);
        return ResponseEntity.ok().build();
    }
}