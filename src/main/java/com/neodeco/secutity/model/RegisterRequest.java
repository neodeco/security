package com.neodeco.secutity.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RegisterRequest  implements Serializable {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
