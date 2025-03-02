package com.subscriptiontracker.model;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private String name;
    private String email;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}