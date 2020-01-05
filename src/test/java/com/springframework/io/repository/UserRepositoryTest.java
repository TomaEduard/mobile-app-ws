package com.springframework.io.repository;

import com.springframework.io.entity.AddressEntity;
import com.springframework.io.entity.UserEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;


@ExtendWith(SpringExtension.class)
@SpringBootTest
class UserRepositoryTest {

    @Autowired
    UserRepository userRepository;

    static boolean recordCreated = false;

    @BeforeEach
    void setUp() {
        if (!recordCreated) {
            createRecords();
        }

    }

    private void createRecords() {
        // Prepare UserEntity to db
        UserEntity userEntity = new UserEntity();
        userEntity.setUserId("1a2b3c");
        userEntity.setFirstName("Eduard");
        userEntity.setLastName("Toma");
        userEntity.setEmail("test@test.com");
        userEntity.setEncryptedPassword("1asd");
        userEntity.setEmailVerificationStatus(true);
        userEntity.setEmailVerificationToken("AS*DO&yghun");

        // Prepare Addresses
        AddressEntity shippingAddress = new AddressEntity();
        shippingAddress.setCity("Alba-Iulia");
        shippingAddress.setCountry("Romania");
        shippingAddress.setStreetName("123 Street name");
        shippingAddress.setAddressId("1a2a3a4a");
        shippingAddress.setPostalCode("ABC123");
        shippingAddress.setType("shipping");
        shippingAddress.setUserDetails(userEntity);

        AddressEntity billingAddress = new AddressEntity();
        billingAddress.setCity("Alba-Iulia");
        billingAddress.setCountry("Romania");
        billingAddress.setAddressId("1b2b3b4b");
        billingAddress.setStreetName("123 Street name");
        billingAddress.setPostalCode("ABC123");
        billingAddress.setType("billing");
        billingAddress.setUserDetails(userEntity);

        List<AddressEntity> addressEntityList = new ArrayList<>();
        addressEntityList.add(shippingAddress);
        addressEntityList.add(billingAddress);

        userEntity.setAddresses(addressEntityList);

        userRepository.save(userEntity);

        //

        // Prepare UserEntity to db
        UserEntity userEntity2 = new UserEntity();
        userEntity2.setUserId("1a2b3c457sr5t");
        userEntity2.setFirstName("Eduard");
        userEntity2.setLastName("Toma");
        userEntity2.setEmail("test@test.com");
        userEntity2.setEncryptedPassword("1asd");
        userEntity2.setEmailVerificationStatus(true);
        userEntity2.setEmailVerificationToken("AS*DO&yghun");

        // Prepare Addresses
        AddressEntity shippingAddress2 = new AddressEntity();
        shippingAddress2.setCity("Alba-Iulia");
        shippingAddress2.setCountry("Romania");
        shippingAddress2.setStreetName("123 Street name");
        shippingAddress2.setAddressId("1a2a3a4a35dgfd");
        shippingAddress2.setPostalCode("ABC123");
        shippingAddress2.setType("shipping");
        shippingAddress2.setUserDetails(userEntity);

        AddressEntity billingAddress2 = new AddressEntity();
        billingAddress2.setCity("Alba-Iulia");
        billingAddress2.setCountry("Romania");
        billingAddress2.setAddressId("1b2b3b4b5as");
        billingAddress2.setStreetName("123 Street name");
        billingAddress2.setPostalCode("ABC123");
        billingAddress2.setType("billing");
        billingAddress2.setUserDetails(userEntity);

        List<AddressEntity> addressEntityList2 = new ArrayList<>();
        addressEntityList2.add(shippingAddress2);
        addressEntityList2.add(billingAddress2);

        userEntity.setAddresses(addressEntityList2);

        userRepository.save(userEntity2);

        recordCreated = true;
    }

    @Test
    final void testGetVerifiedUsers() {
        Pageable pageableRequest = PageRequest.of(0, 2);
//        Page<UserEntity> userEntityPage = userRepository.findAllByEmailVerificationStatusTrue(pageableRequest);
        Page<UserEntity> userEntityPage = userRepository.findAllUsersWithConfirmedEmailAddress(pageableRequest);

        assertNotNull(userEntityPage);

        List<UserEntity> userEntities = userEntityPage.getContent();
        assertEquals(2, userEntities.size());
    }

    @Test
    final void findUserByFirstNameAndLastName() {
        String firstName = "Eduard";
        String lastName = "Toma";

        List<UserEntity> users = userRepository.findUserByFirstNameAndLastName(firstName, lastName);
        assertNotNull(users);
        assertEquals(2, users.size());

        UserEntity userEntity1 = users.get(0);
        assertEquals(firstName, userEntity1.getFirstName());

    }

    @Test
    final void findUserByFirstNameAndLastNameWithNamedParams() {
        String firstName = "Eduard";
        String lastName = "Toma";

        List<UserEntity> users = userRepository.findUserByFirstNameAndLastNameWithNamedParams(firstName, lastName);
        assertNotNull(users);
        assertEquals(2, users.size());

        UserEntity userEntity1 = users.get(0);
        assertEquals(firstName, userEntity1.getFirstName());

    }

    @Test
    final void testFindUserByKeyword() {
        String keyword = "uar";

        List<UserEntity> users = userRepository.findUserByKeyword(keyword);
        assertNotNull(users);
        assertEquals(2, users.size());

        UserEntity userEntity1 = users.get(0);
        assertTrue(userEntity1.getLastName().contains(keyword) || userEntity1.getFirstName().contains(keyword));

    }

    @Test
    final void testFindUserByKeywordByJPAMethod() {
        String keyword = "uar";

        List<UserEntity> users = userRepository.findByFirstNameContainingOrLastNameContaining(keyword, "To");
        assertNotNull(users);
        assertEquals(2, users.size());

        UserEntity userEntity1 = users.get(0);
        assertTrue(userEntity1.getLastName().contains(keyword) || userEntity1.getFirstName().contains(keyword));

    }
}







