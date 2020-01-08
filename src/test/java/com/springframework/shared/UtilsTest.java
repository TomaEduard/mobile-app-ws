package com.springframework.shared;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
class UtilsTest {

    @Autowired
    Utils utils;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateUserId() {
        String userId = utils.generateUserId(30);
        String userId2 = utils.generateUserId(30);

        assertNotNull(userId);
        assertNotNull(userId2);
        assertEquals(30, userId.length());
        assertEquals(30, userId2.length());
        assertFalse(userId.equalsIgnoreCase(userId2));
    }

    @Test
    void testHasTokenNotExpired() {
        String token = utils.generateEmailVerificationToken("poAINUSDMJuy8");
        assertNotNull(token);

        boolean hasTokenExpired = Utils.hasTokenExpired(token);

        assertFalse(hasTokenExpired);
    }

    @Test
    void testHasTokenExpired() {

        String expiredToken = utils.generateExpiredTokenForTest();

        boolean hasTokenExpired = Utils.hasTokenExpired(expiredToken);

        // true is expired
        assertTrue(hasTokenExpired);
    }

}


















