package com.springframework.service.impl;

import com.springframework.exceptions.UserServiceException;
import com.springframework.io.entity.AddressEntity;
import com.springframework.io.entity.UserEntity;
import com.springframework.io.repository.UserRepository;
import com.springframework.shared.AmazonSES;
import com.springframework.shared.Utils;
import com.springframework.shared.dto.AddressDto;
import com.springframework.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Mock
    UserRepository userRepository;

    @Mock
    Utils utils;

    @Mock
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    AmazonSES amazonSES;

    String USER_ID = "kjdsf89123kls";
    String ADDRESS_ID = "vhb80973u9ipjom";
    String ENCRYPTED_PASSWORD = "1jkld823klsf";
    String EMAIL_VERIFICATION_TOKEN = "lvYe915JKMVCuclasdikgV";

    UserEntity userEntity;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setUserId(USER_ID);
        userEntity.setFirstName("Eduard");
        userEntity.setLastName("Toma");
        userEntity.setEmail("test@test.com");
        userEntity.setEmailVerificationToken("5sTY9hijuokw");
        userEntity.setEncryptedPassword(ENCRYPTED_PASSWORD);
        userEntity.setEmailVerificationToken(EMAIL_VERIFICATION_TOKEN);
        userEntity.setAddresses(getAddressesEntity());
    }

    private List<AddressDto> getAddressesDTO() {

        AddressDto addressDTO = new AddressDto();
        addressDTO.setCity("Alba-Iulia");
        addressDTO.setCountry("Romania");
        addressDTO.setStreetName("123 Street name");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setType("shipping");

        AddressDto billingAddressDto = new AddressDto();
        addressDTO.setCity("Alba-Iulia");
        addressDTO.setCountry("Romania");
        addressDTO.setStreetName("123 Street name");
        addressDTO.setPostalCode("ABC123");
        addressDTO.setType("billing");

        // cr8 addressDTOList and add addressDTO to it
        List<AddressDto> addressDtoList = new ArrayList<>();
        addressDtoList.add(addressDTO);
        addressDtoList.add(billingAddressDto);

        return addressDtoList;
    }

    private List<AddressEntity> getAddressesEntity() {

        List<AddressDto> addressDTOS = getAddressesDTO();
        Type listType = new TypeToken<List<AddressEntity>>() {}.getType();

        return new ModelMapper().map(addressDTOS, listType);
    }

    @Test
    final void testGetUser() {

        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = userServiceImpl.getUser("test@test.com");

        assertNotNull(userDto);
        assertEquals("Eduard", userDto.getFirstName());
    }

    @Test
    final void testGetUser_UserNotFoundException() {
        when(userRepository.findByEmail(anyString())).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userServiceImpl.getUser("test@test.com"));
    }

    @Test
    final void testCreateUser() {
        // mock methods
        when(userRepository.findByEmail(anyString())).thenReturn(null);
        when(utils.generateAddressId(anyInt())).thenReturn(ADDRESS_ID);
        when(utils.generateUserId(anyInt())).thenReturn(USER_ID);
        when(bCryptPasswordEncoder.encode(anyString())).thenReturn(ENCRYPTED_PASSWORD);
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        doNothing().when(amazonSES).verifyEmail(any(UserDto.class));

        // cr8 userDto and add addresses to it
        UserDto userDto = new UserDto();
        userDto.setFirstName("Eduard");
        userDto.setLastName("Toma");
        userDto.setEmail("test@test.com");
        userDto.setPassword("12345678");

        userDto.setAddresses(getAddressesDTO());

        UserDto storedUserDto = userServiceImpl.createUser(userDto);

        assertNotNull(storedUserDto);
        assertEquals(userEntity.getFirstName(), storedUserDto.getFirstName());
        assertEquals(userEntity.getLastName(), storedUserDto.getLastName());
        assertNotNull(storedUserDto.getUserId());
        assertEquals(storedUserDto.getAddresses().size(), userEntity.getAddresses().size());
//        verify(utils, times(2)).generateAddressId(30);
        verify(utils, times(storedUserDto.getAddresses().size())).generateAddressId(30);
        verify(bCryptPasswordEncoder, times(1)).encode("12345678");
        verify(userRepository, times(1)).save(any(UserEntity.class));
    }

    @Test
    final void testCreateUser_CreateUserServiceException() {
        when(userRepository.findByEmail(anyString())).thenReturn(userEntity);

        UserDto userDto = new UserDto();
        userDto.setFirstName("Eduard");
        userDto.setLastName("Toma");
        userDto.setEmail("test@test.com");
        userDto.setPassword("12345678");

        assertThrows(UserServiceException.class, () -> userServiceImpl.createUser(userDto));


    }

}















