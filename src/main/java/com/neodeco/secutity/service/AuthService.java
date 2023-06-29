package com.neodeco.secutity.service;


import com.neodeco.secutity.model.AuthRequest;
import com.neodeco.secutity.model.AuthResponse;
import com.neodeco.secutity.model.RegisterRequest;

public interface AuthService {
    AuthResponse authenticate(AuthRequest request);
    AuthResponse register(RegisterRequest request);
}
