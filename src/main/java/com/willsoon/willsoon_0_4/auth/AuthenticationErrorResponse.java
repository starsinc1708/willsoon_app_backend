package com.willsoon.willsoon_0_4.auth;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationErrorResponse extends AuthenticationResponse{
    private String errorBody;
}
