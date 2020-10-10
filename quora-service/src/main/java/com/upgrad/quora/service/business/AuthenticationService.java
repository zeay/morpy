package com.upgrad.quora.service.business;

import com.upgrad.quora.service.dao.UserDao;
import com.upgrad.quora.service.entity.UserAuthEntity;
import com.upgrad.quora.service.entity.UserEntity;
import com.upgrad.quora.service.exception.AuthenticationFailedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class AuthenticationService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider cryptographyProvider;


    @Transactional
    public UserAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {

        UserEntity userEntity = userDao.getUserByUserName(username);
        if(userEntity == null){
            throw new AuthenticationFailedException("ATH-001", "This username does not exist");
        }

        String encryptedPassword =  cryptographyProvider.encrypt(password, userEntity.getSalt());
        if(userEntity.getPassword().equals(encryptedPassword)){

            JwtTokenProvider tokenProvider = new JwtTokenProvider(encryptedPassword);
            final ZonedDateTime now = ZonedDateTime.now();
            final ZonedDateTime expiresAt = now.plusHours(8);

            UserAuthEntity userAuthEntity = new UserAuthEntity();
            userAuthEntity.setUserEntity(userEntity);
            userAuthEntity.setUuid(UUID.randomUUID().toString());
            userAuthEntity.setAccessToken(tokenProvider.generateToken(userEntity.getUuid(),now,expiresAt));
            userAuthEntity.setLoginAt(now);
            userAuthEntity.setExpiresAt(expiresAt);

            return userDao.createAuthToken(userAuthEntity);

        }else{
            throw new AuthenticationFailedException("ATH-002","Password failed");
        }
    }
}
