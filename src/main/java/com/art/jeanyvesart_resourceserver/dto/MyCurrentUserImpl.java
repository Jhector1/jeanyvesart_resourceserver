package com.art.jeanyvesart_resourceserver.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyCurrentUserImpl implements MyCurrentUser {
    private String fullName;
    private String id;

}
