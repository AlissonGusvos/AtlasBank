package com.atlasbk.Atlas.User;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
public class User {

    @Id
    private Long id;

    private String email;

    @Column(name = "hash_pass")
    private String hashPass;

    private String cpf;
    private String username;
}
