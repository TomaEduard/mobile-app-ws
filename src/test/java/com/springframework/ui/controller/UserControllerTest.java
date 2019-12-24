package com.springframework.ui.controller;

import com.springframework.service.impl.UserServiceImpl;
import com.springframework.shared.dto.AddressDTO;
import com.springframework.shared.dto.UserDto;
import com.springframework.ui.transfer.response.UserRest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

class UserControllerTest {

    @InjectMocks
    UserController userController;

    @Mock
    UserServiceImpl userServiceImpl;

    String USER_ID = "kjdsf89123kls";
    String ENCRYPTED_PASSWORD = "1jkld823klsf";

    UserDto userDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userDto = new UserDto();
        userDto.setUserId(USER_ID);
        userDto.setFirstName("Eduard");
        userDto.setLastName("Toma");
        userDto.setEmail("test@test.com");
        userDto.setPassword("123");
        userDto.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userDto.setEmailVerificationToken(null);
        userDto.setEmailVerificationStatus(Boolean.FALSE);
        userDto.setAddresses(getAddressesDTO());

    }

    private List<AddressDTO> getAddressesDTO() {

        AddressDTO addressDTO = new AddressDTO();
        addressDTO.setCity("Alba-Iulia");
        addressDTO.setCountry("Romania");
        addressDTO.setStreetName("123 Street name");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setType("shipping");

        AddressDTO BillingAddressDTO = new AddressDTO();
        addressDTO.setCity("Alba-Iulia");
        addressDTO.setCountry("Romania");
        addressDTO.setStreetName("123 Street name");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setType("billing");

        // cr8 addressDTOList and add addressDTO to it
        List<AddressDTO> addressDTOList = new ArrayList<>();
        addressDTOList.add(addressDTO);
        addressDTOList.add(BillingAddressDTO);

        return addressDTOList;
    }

    @Test
    final void getUser() {

        when(userServiceImpl.getUserByUserId(anyString())).thenReturn(userDto);

        UserRest userRest = userController.getUser(USER_ID);

        assertNotNull(userRest);
        assertEquals(USER_ID, userRest.getUserId());
        assertEquals(userDto.getFirstName(), userRest.getFirstName());
        assertEquals(userDto.getLastName(), userRest.getLastName());
        assertEquals(userDto.getAddresses().size(), userRest.getAddresses().size());
    }



}