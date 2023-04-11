package com.willsoon.willsoon_0_4.entity.AppUser;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
public class AppUserPojo {

    private UUID id;
    private String username;
    private String email;
    private Boolean active;
}
