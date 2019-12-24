package com.springframework.shared;

import com.springframework.security.SecurityConstants;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import javax.xml.crypto.Data;
import java.security.SecureRandom;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.ZonedDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

@Component
public class Utils {

    private final Random RANDOM = new SecureRandom();

    public String generateUserId(int length) {
        return generateRandomString(length);
    }

    public String generateAddressId(int length) {
        return generateRandomString(length);
    }

    private String generateRandomString(int length) {
        StringBuilder returnValue = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            String ALPHABET = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwzyz";
            // randomly pickup a character of ALPHABET String length number of time
            returnValue.append(ALPHABET.charAt(RANDOM.nextInt(ALPHABET.length())));
        }

        return new String(returnValue);
    }

    public static boolean hasTokenExpired(String token) {
        boolean returnValue = false;

        try {
            Claims claims = Jwts.parser()
                    // use getTokenSecret() method from SecurityConstants to get the tokenSecret from application.propetie
                    .setSigningKey(SecurityConstants.getTokenSecret())
                    // decrypt token and save to local variable named claims
                    .parseClaimsJws(token).getBody();

            Date tokenExpirationDate = claims.getExpiration();
            Date todayDate = new Date();

            // if return true the date is expired
            returnValue = tokenExpirationDate.before(todayDate);
            System.out.println("tokenExpirationDate :" + tokenExpirationDate);
            System.out.println("todayDate :" + todayDate);
            System.out.println("returnValue :" + returnValue);
        } catch (ExpiredJwtException ex) {
            returnValue = true;
        }

        return returnValue;
    }

    public String generateExpiredTokenForTest() {

        return Jwts.builder()
                .setExpiration(new Date(System.currentTimeMillis() - SecurityConstants.EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();
    }

    public String generateEmailVerificationToken(String publicUserId) {
        String token = Jwts.builder()
                .setSubject(publicUserId)
                // generate a Date from today + SecurityConstants.EXPIRATION_TIME(10days) which is named Expiration
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.EXPIRATION_TIME))
                // use getTokenSecret() method from SecurityConstants to get the tokenSecret from application.propetie
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        return token;
    }

    public static String generatePasswordResetToken(String publicUserId) {
        String token = Jwts.builder()
                .setSubject(publicUserId)
                // generate a Date from today + SecurityConstants.EXPIRATION_TIME(10days) which is named Expiration
                .setExpiration(new Date(System.currentTimeMillis() + SecurityConstants.PASSWORD_RESET_EXPIRATION_TIME))
                // use getTokenSecret() method from SecurityConstants to get the tokenSecret from application.propetie
                .signWith(SignatureAlgorithm.HS512, SecurityConstants.getTokenSecret())
                .compact();

        return token;
    }
}

