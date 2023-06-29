package com.neodeco.secutity.service.impl;


import com.neodeco.secutity.config.JwtService;
import com.neodeco.secutity.domains.TokenEntity;
import com.neodeco.secutity.domains.UserEntity;
import com.neodeco.secutity.domains.enums.TokenType;
import com.neodeco.secutity.domains.enums.UserRole;
import com.neodeco.secutity.model.AuthRequest;
import com.neodeco.secutity.model.AuthResponse;
import com.neodeco.secutity.model.RegisterRequest;
import com.neodeco.secutity.repository.TokenRepository;
import com.neodeco.secutity.repository.UserRepository;
import com.neodeco.secutity.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final TokenRepository tokenRepository;
    private final PasswordEncoder encoder;
    private final JwtService service;
    private final AuthenticationManager authenticationManager;

    @Autowired
    public AuthServiceImpl(UserRepository userRepository,
                           TokenRepository tokenRepository,
                           PasswordEncoder encoder,
                           JwtService jwtAuthSevice,
                           AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.encoder = encoder;
        this.service = jwtAuthSevice;
        this.authenticationManager = authenticationManager;
    }

    @Override
    public AuthResponse authenticate(AuthRequest request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(request.getEmail()
                , request.getPassword()));

        var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
        var token = service.generateToken(user);

        revokeAllUserTokens(user);

        saveUserToken(user, token);

        return AuthResponse.builder().token(token).build();
    }

    @Override
    public AuthResponse register(RegisterRequest request) {
        UserEntity savedUser = saveUser(request);

        var token = service.generateToken(savedUser);

        saveUserToken(savedUser, token);

        return AuthResponse.builder().token(token).build();
    }

    private void revokeAllUserTokens(UserEntity user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if (validUserTokens.isEmpty()) return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    private UserEntity saveUser(RegisterRequest request) {
        var user = UserEntity.builder()
                .firstName(request.getFirstName())
                .email(request.getEmail())
                .password(encoder.encode(request.getPassword()))
                .role(UserRole.USER)
                .build();

        return userRepository.save(user);
    }

    private void saveUserToken(UserEntity savedUser, String token) {
        var userToken = TokenEntity.builder()
                .user(savedUser)
                .token(token)
                .tokenType(TokenType.BEARER)
                .revoked(false)
                .expired(false)
                .build();
        tokenRepository.save(userToken);
    }
}
