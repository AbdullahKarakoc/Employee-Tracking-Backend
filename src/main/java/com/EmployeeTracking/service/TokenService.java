package com.EmployeeTracking.service;

import com.EmployeeTracking.domain.model.Token;
import com.EmployeeTracking.exception.InvalidTokenException;
import com.EmployeeTracking.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final TokenRepository tokenRepository;


    public Token findByToken(String token) {
        return tokenRepository.findByToken(token)
                .orElseThrow(() -> new InvalidTokenException("Invalid token"));
    }

    public Token save(Token token) {
        return tokenRepository.saveAndFlush(token);
    }

}
