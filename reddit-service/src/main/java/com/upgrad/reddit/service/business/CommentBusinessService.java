package com.upgrad.reddit.service.business;

import com.upgrad.reddit.service.dao.CommentDao;
import com.upgrad.reddit.service.dao.UserDao;
import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserAuthEntity;
import com.upgrad.reddit.service.exception.AuthorizationFailedException;
import com.upgrad.reddit.service.exception.CommentNotFoundException;
import com.upgrad.reddit.service.exception.InvalidPostException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.TypedQuery;

@Service
public class CommentBusinessService {


    @Autowired
    private UserDao userDao;

    @Autowired
    private CommentDao commentDao;


    /**
     * The method implements the business logic for createComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity createComment(CommentEntity commentEntity, String authorization) throws AuthorizationFailedException {

        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
    }

    public PostEntity getPostByUuid(String Uuid) throws InvalidPostException {

        PostEntity postEntity = commentDao.getPostByUuid(Uuid);

    }


    /**
     * The method implements the business logic for editCommentContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity editCommentContent(CommentEntity commentEntity, String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

    }

    /**
     * The method implements the business logic for deleteComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity deleteComment(String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

    }

    /**
     * The method implements the business logic for getAllCommentsToPost endpoint.
     */
    public TypedQuery<CommentEntity> getCommentsByPost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);

    }
}
