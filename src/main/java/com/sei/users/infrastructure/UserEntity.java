package com.sei.users.infrastructure;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Table("users")
public class UserEntity {
    @Id
    private UUID id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;

}
