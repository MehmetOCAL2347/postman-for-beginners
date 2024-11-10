package com.udemy.postman.User.business.manager;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import com.udemy.postman.User.business.service.TokenService;
import com.udemy.postman.User.entities.entity.UserEntity;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
@NoArgsConstructor
public class TokenManager implements TokenService {

    @Autowired
    private JwtEncoder jwtEncoder;

    private final long EXPIRATION_TIME = 60000; // 20 sn, 1 second = 1000 milliseconds

    @Override
    public String generateJwt(UserEntity userEntity) {
        String userEmail = userEntity.getEmail();
        JwtClaimsSet claims;

        String scope = userEntity.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRATION_TIME, ChronoUnit.MILLIS);

        claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(userEmail)
                .claim("id", userEntity.getId())
                .claim("roles", scope)
                .claim("email", userEmail)
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String generateJwt(Authentication auth) {

        String scope = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));

        UserEntity userEntity = (UserEntity) auth.getPrincipal();

        Instant now = Instant.now();
        Instant expiration = now.plus(EXPIRATION_TIME, ChronoUnit.MILLIS);

        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(expiration)
                .subject(userEntity.getUsername())
                .claim("id", userEntity.getId())
                .claim("roles", scope)
                .claim("email", userEntity.getUsername())
                .build();

        return jwtEncoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    @Override
    public String getToken(String token) {
        String[] parts = token.split(" ");
        String splittedToken = "";

        if (parts.length == 2 && parts[0].equalsIgnoreCase("Bearer")) {
            splittedToken = parts[1];
        }
        return splittedToken;
    }

    @Override
    public String getUserIdFromJwt(String jwt) {
        JWTClaimsSet claims = null;
        try {
            claims = getClaimSetFromJwt(jwt);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            //throw new RuntimeException(e);
        }
        return claims.getClaim("id").toString();
    }

    public JWTClaimsSet getClaimSetFromJwt(String jwt) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(jwt);
        return signedJWT.getJWTClaimsSet();
    }
}
