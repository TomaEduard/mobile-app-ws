package com.springframework.service.impl;

import com.springframework.exceptions.UserServiceException;
import com.springframework.io.entity.UserEntity;
import com.springframework.io.repository.UserRepository;
import com.springframework.service.UserService;
import com.springframework.shared.Utils;
import com.springframework.shared.dto.AddressDTO;
import com.springframework.shared.dto.UserDto;
import com.springframework.ui.transfer.response.ErrorMessages;
import org.modelmapper.ModelMapper;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    Utils utils;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDto createUser(UserDto userDto) {

        // check if the email already exist in db
        if (userRepository.findByEmail(userDto.getEmail()) != null) throw new RuntimeException("Record is already exists!");

        // generateAddressId for every address
        for (int i = 0; i< userDto.getAddresses().size(); i++){
            AddressDTO address = userDto.getAddresses().get(i);
            address.setUserDetails(userDto);
            address.setAddressId(utils.generateAddressId(30));

            userDto.getAddresses().set(i, address);
        }

//        UserEntity userEntity = new UserEntity();
//        BeanUtils.copyProperties(userDto, userEntity);
            ModelMapper modelMapper = new ModelMapper();
        UserEntity userEntity = modelMapper.map(userDto, UserEntity.class);

        // set userId and generated random sting
        String publicUserId = utils.generateUserId(30);

        userEntity.setUserId(publicUserId);
        userEntity.setEncryptedPassword(bCryptPasswordEncoder.encode((userDto.getPassword())));
        userEntity.setEmailVerificationToken(utils.generateEmailVerificationToken(publicUserId));
        userEntity.setEmailVerificationStatus(false);

        UserEntity storedUserDetails = userRepository.save(userEntity);

//        UserDto returnValue = new UserDto();
//        BeanUtils.copyProperties(storedUserDetails, returnValue);
        UserDto returnValue = modelMapper.map(storedUserDetails, UserDto.class);

        return returnValue;
    }

    @Override
    public UserDto getUser(String email) {

        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto getUserByUserId(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) throw new UsernameNotFoundException("User with id: " + userId + " not found");

        UserDto userDto = new UserDto();
        BeanUtils.copyProperties(userEntity, userDto);

        return userDto;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userEntity.setFirstName(userDto.getFirstName());
        userEntity.setLastName(userDto.getLastName());

        UserEntity updatedUserDetails = userRepository.save(userEntity);

        UserDto returnValue = new UserDto();
        BeanUtils.copyProperties(updatedUserDetails, returnValue);

        return returnValue;
    }

    @Override
    public void deleteUser(String userId) {

        UserEntity userEntity = userRepository.findByUserId(userId);
        if (userEntity == null) {
            throw new UserServiceException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());
        }

        userRepository.delete(userEntity);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {

//        if (page > 0) page -= 1;

        PageRequest pageableRequest = PageRequest.of(page, limit);

        Page<UserEntity> usersPage = userRepository.findAll(pageableRequest);
        List<UserEntity> users = usersPage.getContent();

        List<UserDto> returnValue = new ArrayList<>();

        for (UserEntity userEntity : users) {
            UserDto userDto = new UserDto();
            BeanUtils.copyProperties(userEntity, userDto);
            returnValue.add(userDto);
        }

        return returnValue;
    }

    @Override
    public boolean verifyEmailToken(String token) {

        boolean returnValue = false;

        // find user by token
        UserEntity userEntity = userRepository.findUserByEmailVerificationToken(token);

        if (userEntity != null) {
            // verify token expire date
            boolean hastokenExpired = Utils.hasTokenExpired(token);

            if (!hastokenExpired) {
                userEntity.setEmailVerificationToken(null);
                userEntity.setEmailVerificationStatus(Boolean.TRUE);
                userRepository.save(userEntity);
                returnValue = true;
            }
        }

        return returnValue;
    }

    // #2 of 3 find the username(email) in the database
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        System.out.println("#2 - UserServiceImpl - loadUserByUsername");
        UserEntity userEntity = userRepository.findByEmail(email);

        if (userEntity == null) throw new UsernameNotFoundException(email);

        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), userEntity.getEmailVerificationStatus(),
        true, true, true, new ArrayList<>());

//        return new User(userEntity.getEmail(), userEntity.getEncryptedPassword(), new ArrayList<>());
    }


}