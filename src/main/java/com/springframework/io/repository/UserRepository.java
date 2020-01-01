package com.springframework.io.repository;


import com.springframework.io.entity.UserEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {

    UserEntity findByEmail(String email);

//    @Query(value="SELECT * FROM mobile_app_ws.users WHERE user_id = '9EQpcgIIG2dm36HQweA7rd4RT4aqv0'",nativeQuery=true)
    UserEntity findByUserId(String userId);

    UserEntity findUserByEmailVerificationToken(String token);

}
