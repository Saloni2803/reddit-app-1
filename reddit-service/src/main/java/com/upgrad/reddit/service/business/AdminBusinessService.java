package com.upgrad.reddit.service.business;

import com.upgrad.reddit.service.dao.AdminDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AdminBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private AdminDao adminDao;

    /**
     * The method implements the business logic for userDelete endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public UserEntity deleteUser(String authorization, String uuid) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "User has not signed in");
        }  else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out.");
        } else if (!userAuthEntity.getUser().getRole().equalsIgnoreCase("admin")) {
            throw new AuthorizationFailedException("ATHR-003", "Unauthorized access, Entered user is not an admin");
        }
        UserEntity userToDelete = userDao.getUserByUuid(uuid);
        if(userToDelete == null){
            throw new UserNotFoundException("USR-001","User with entered uuid to be deleted does not exist");
        }
        UserEntity userEntity = adminDao.deleteUser(userToDelete);
        return userEntity;
    }
}
