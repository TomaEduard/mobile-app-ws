package com.springframework.service.impl;

import com.springframework.io.entity.AddressEntity;
import com.springframework.io.entity.UserEntity;
import com.springframework.io.repository.AddressRepository;
import com.springframework.io.repository.UserRepository;
import com.springframework.shared.dto.AddressDTO;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class AddressService implements com.springframework.service.AddressService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AddressRepository addressRepository;

    @Override
    public List<AddressDTO> getAddresses(String userId) {

        List<AddressDTO> returnValue = new ArrayList<>();
        ModelMapper modelMapper = new ModelMapper();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) return returnValue;

        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
        for (AddressEntity addressEntity : addresses) {
            returnValue.add(modelMapper.map(addressEntity, AddressDTO.class));
        }

        return returnValue;
    }

    public void getAddressesByUserid(String userId) {

//        List<AddressEntity> allAddressesByuserId = addressRepository.findAllByUserDetails(userId);

        UserEntity userEntity = userRepository.findByUserId(userId);
        System.out.println(userEntity.getAddresses().get(2));
//        Iterable<AddressEntity> addresses = addressRepository.findAllByUserDetails(userEntity);
    }

    @Override
    public AddressDTO getAddress(String addressId) {

        AddressEntity addressEntity = addressRepository.findByAddressId(addressId);

        AddressDTO returnValue = null;
        if (addressEntity != null) returnValue = new ModelMapper().map(addressEntity, AddressDTO.class);

        return returnValue;
    }

}
