package com.springframework.service;

import com.springframework.shared.dto.AddressDTO;
import org.springframework.stereotype.Service;

import java.util.List;

public interface AddressService {

    List<AddressDTO> getAddresses(String userId);

    AddressDTO getAddress(String addressId);
}
