package com.springframework.service.impl;

import com.springframework.io.entity.AddressEntity;
import com.springframework.io.entity.UserEntity;
import com.springframework.io.repository.AddressRepository;
import com.springframework.io.repository.UserRepository;
import com.springframework.service.AddressService;
import com.springframework.shared.dto.AddressDto;
import com.springframework.ui.transfer.response.UserRest;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDto> getAddresses(String userId) {

        List<AddressDto> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        // get UserEntity by userId
        UserEntity userEntity = userRepository.findByUserId(userId);

        // get AddressEntity by userEntity relationship
        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);

        for(AddressEntity addressEntity:addresses){
            returnValue.add( modelMapper.map(addressEntity, AddressDto.class) );
        }

        return returnValue;
    }

    @Override
    public AddressDto getAddress(String addressId) {

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        AddressDto returnValue = null;
        if (addressEntity != null) returnValue = new ModelMapper().map(addressEntity, AddressDto.class);

        System.out.println("*** - " + returnValue);
        return returnValue;
    }

}











