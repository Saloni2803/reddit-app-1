package com.upgrad.reddit.service.business;

import com.upgrad.reddit.service.common.JwtTokenProvider;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthenticationFailedException;
import com.upgrad.reddit.service.exception.SignOutRestrictedException;
import com.upgrad.reddit.service.exception.SignUpRestrictedException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class UserBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PasswordCryptographyProvider passwordCryptographyProvider;

    /**
     * The method implements the business logic for signup endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity signup(UserEntity userEntity) throws SignUpRestrictedException {
        String[] encryptedText = passwordCryptographyProvider.encrypt(userEntity.getPassword());
        userEntity.setSalt(encryptedText[0]);
        userEntity.setPassword(encryptedText[1]);
        userEntity.setRole("nonadmin");
        userEntity.setUuid(UUID.randomUUID().toString());
        UserEntity userName = userDao.getUserByUsername(userEntity.getUserName());
        UserEntity email = userDao.getUserByEmail(userEntity.getEmail());
        if (userName != null)
            throw new SignUpRestrictedException("SGR-001", "Try any other Username, This user name has already been taken");
        if (email != null)
            throw new SignUpRestrictedException("SGR-002", "This user has already been registered, try with another emailId");
        UserEntity user = userDao.createUser(userEntity);
        return user;
    }

    /**
     * The method implements the business logic for signin endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity authenticate(String username, String password) throws AuthenticationFailedException {
        UserAuthEntity userAuthEntity = null;
        UserEntity userEntity = userDao.getUserByUsername(username);
        if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-001", "This user name does not exist");
        } else {
            String encryptedText = passwordCryptographyProvider.encrypt(password,userEntity.getSalt());
            userEntity = userDao.authenticate(username, encryptedText);
        } if (userEntity == null) {
            throw new AuthenticationFailedException("ATH-002", "Password failed");
        } else {
            userAuthEntity = createUserAuth(userEntity);
            userDao.createUserAuth(userAuthEntity);
        }
        return userAuthEntity;
    }

    /**
     * The method implements the business logic for signout endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserAuthEntity signout(String authorization) throws SignOutRestrictedException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if(userAuthEntity == null) throw new SignOutRestrictedException("SGR-001", "User is not signed in");
        userAuthEntity.setLogoutAt(ZonedDateTime.now());
        userDao.updateUserAuth(userAuthEntity);
        return userAuthEntity;
    }

    private UserAuthEntity createUserAuth(UserEntity userEntity){
        UserAuthEntity userAuthEntity = new UserAuthEntity();

        JwtTokenProvider tokenProvider = new JwtTokenProvider(userEntity.getSalt());

        ZonedDateTime issuedDateTime = ZonedDateTime.now();
        ZonedDateTime expiresDateTime = issuedDateTime.plusHours(1);
        String accessToken = tokenProvider.generateToken(userEntity.getUuid(), issuedDateTime, expiresDateTime);

        userAuthEntity.setAccessToken(accessToken);
        userAuthEntity.setUuid(userEntity.getUuid());
        userAuthEntity.setUser(userEntity);
        userAuthEntity.setExpiresAt(expiresDateTime);
        userAuthEntity.setLoginAt(issuedDateTime);
        userAuthEntity.setLogoutAt(null);

        return  userAuthEntity;
    }
}
