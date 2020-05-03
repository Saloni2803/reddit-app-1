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
import java.time.ZonedDateTime;
import java.util.UUID;

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
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to post a comment");
        }
        commentEntity.setDate(ZonedDateTime.now());
        commentEntity.setUser(userAuthEntity.getUser());
        commentEntity.setUuid(UUID.randomUUID().toString());
        CommentEntity comment = commentDao.createComment(commentEntity);
        return comment;
    }

    public PostEntity getPostByUuid(String Uuid) throws InvalidPostException {
        PostEntity postEntity = commentDao.getPostByUuid(Uuid);
        return postEntity;
    }


    /**
     * The method implements the business logic for editCommentContent endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity editCommentContent(CommentEntity commentEntity, String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to edit a comment");
        }
        CommentEntity commentToBeEdited = commentDao.getCommentByUuid(commentId);
        if (commentToBeEdited == null) {
            throw new CommentNotFoundException("COM-001", "Entered comment uuid does not exist");
        }
        if (!commentToBeEdited.getUser().getUserName().equalsIgnoreCase(userAuthEntity.getUser().getUserName())) {
            throw new AuthorizationFailedException("ATHR-003", "Only the comment owner can edit the comment");
        }
        commentEntity.setUuid(UUID.randomUUID().toString());
        commentEntity.setDate(ZonedDateTime.now());
        commentEntity.setUser(userAuthEntity.getUser());
        commentEntity.setPost(commentToBeEdited.getPost());
        CommentEntity commentEdited = commentDao.editComment(commentEntity);
        return commentEdited;

    }

    /**
     * The method implements the business logic for deleteComment endpoint.
     */
    @Transactional(propagation = Propagation.REQUIRED)
    public CommentEntity deleteComment(String commentId, String authorization) throws AuthorizationFailedException, CommentNotFoundException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to delete a comment");
        }
        CommentEntity commentToBeDeleted = commentDao.getCommentByUuid(commentId);
        if (commentToBeDeleted == null) {
            throw new CommentNotFoundException("COM-001", "Entered commment uuid does not exit");
        }
        if (!userAuthEntity.getUser().getRole().equalsIgnoreCase("admin")) {
            if(!commentToBeDeleted.getUser().getUserName().equalsIgnoreCase(userAuthEntity.getUser().getUserName())){
                throw new AuthorizationFailedException("ATHR-003", "Only the comment owner or admin can delete the comment");
            }
        }
        CommentEntity commetDeleted = commentDao.deleteComment(commentToBeDeleted);
        return commetDeleted;
    }

    /**
     * The method implements the business logic for getAllCommentsToPost endpoint.
     */
    public TypedQuery<CommentEntity> getCommentsByPost(String postId, String authorization) throws AuthorizationFailedException, InvalidPostException {
        UserAuthEntity userAuthEntity = userDao.getUserAuthByAccesstoken(authorization);
        if (userAuthEntity == null) {
            throw new AuthorizationFailedException("ATHR-001", "USER HAS NOT SIGNED IN");
        } else if (userAuthEntity.getLogoutAt() != null) {
            throw new AuthorizationFailedException("ATHR-002", "User is Signed Out. Sign in first to get the comments");
        }
        PostEntity post = commentDao.getPostByUuid(postId);
        if (post == null) {
            throw new InvalidPostException("POS-001", "The post with entered uuid whose details are to be seen does not exist");
        }
        TypedQuery<CommentEntity> comments = commentDao.getCommentsByPost(post);
        return comments;
    }
}
