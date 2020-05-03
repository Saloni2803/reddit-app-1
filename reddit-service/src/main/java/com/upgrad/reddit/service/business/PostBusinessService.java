package com.upgrad.reddit.service.business;


import com.upgrad.reddit.service.dao.PostDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import com.upgrad.reddit.service.exception.UserNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;
import java.time.ZonedDateTime;
import java.util.UUID;

@Service
public class PostBusinessService {

    @Autowired
    private UserDao userDao;

    @Autowired
    private PostDao postDao;


    /**
     * The method implements the business logic for createPost endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity createPost(PostEntity postEntity, String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to create a post");
        }
        postEntity.setUser(userAuthEntity.getUser());
        postEntity.setUuid(UUID.randomUUID().toString());
        postEntity.setDate(ZonedDateTime.now());
        PostEntity postCreated = postDao.createPost(postEntity);
        return postCreated;
    }

    /**
     * The method implements the business logic for getAllPosts endpoint.
     */
    public TypedQuery<PostEntity> getPosts(String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in to get all posts");
        }
        TypedQuery<PostEntity> posts = postDao.getPosts();
        return posts;
    }

    /**
     * The method implements the business logic for editPostContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity editPostContent(PostEntity postEntity, String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        PostEntity postTobeEdited = postDao.getPostByUuid(postId);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to edit the post");
        } else {
            if (postTobeEdited == null) {
                throw new InvalidPostException("POS-001", "Entered Post uuid does not exist");
            } else if (!postTobeEdited.getUser().getUserName().equalsIgnoreCase(userAuthEntity.getUser().getUserName())) {
                throw new AuthorizationFailedException("ATHR-003", "Only the post owner can edit the post");
            }
        }

        postEntity.setUser(userAuthEntity.getUser());
        postEntity.setDate(ZonedDateTime.now());
        PostEntity post = postDao.editPost(postEntity);
        return post;

    }

    /**
     * The method implements the business logic for deletePost endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public PostEntity deletePost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        PostEntity postTobeDeleted = postDao.getPostByUuid(postId);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to delete a post");
        } else {
            if (postTobeDeleted == null) {
                throw new InvalidPostException("POS-001", "Entered Post uuid does not exist");
            } else if (!userAuthEntity.getUser().getRole().equalsIgnoreCase("admin")) {
                if (!postTobeDeleted.getUser().getUserName().equalsIgnoreCase(userAuthEntity.getUser().getUserName())) {
                    throw new AuthorizationFailedException("ATHR-003", "Only the post owner or admin can delete the post");
                }

            }
        }
        PostEntity postDeleted = postDao.deletePost(postTobeDeleted);
        return postDeleted;
    }

    /**
     * The method implements the business logic for getAllPostsByUser endpoint.
     */
    public TypedQuery<PostEntity> getPostsByUser(String userId, String authorization) throws AuthorizationFailedException, UserNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to get all posts posted by a specific user");
        }

        UserEntity userEntity = userDao.getUserByUuid(userId);
        if (userEntity == null) {
            throw new UserNotFoundException("USR-001", "User with the entered uuid whose post details are to be seen does not exist");
        }
        TypedQuery<PostEntity> posts = postDao.getPostsByUser(userEntity);
        return posts;
    }

}
