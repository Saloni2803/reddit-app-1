package com.upgrad.reddit.service.dao;


import com.upgrad.reddit.service.entity.CommentEntity;
import com.upgrad.reddit.service.entity.PostEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * CommentDao class provides the database access for all the endpoints in comment controller.
 */
@Repository
public class CommentDao {

    @PersistenceContext
    private EntityManager entityManager;


    public CommentEntity createComment(CommentEntity commentEntity) {
        entityManager.persist(commentEntity);
        return commentEntity;
    }

    public PostEntity getPostByUuid(String uuid) {
        try {
            return entityManager.createNamedQuery("postByUuid", PostEntity.class).setParameter("uuid", uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CommentEntity getCommentByUuid(String commentId) {
        try {

            return entityManager.createNamedQuery("commentByUuid", CommentEntity.class).setParameter("uuid", commentId).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public CommentEntity editComment(CommentEntity commentEntity) {
        return entityManager.merge(commentEntity);
    }

    public CommentEntity deleteComment(CommentEntity commentEntity) {
        entityManager.remove(commentEntity);
        return commentEntity;
    }

    public TypedQuery<CommentEntity> getCommentsByPost(PostEntity postEntity) {
        try {
            return entityManager.createNamedQuery("getAllCommentsByPost", CommentEntity.class).setParameter("post", postEntity);
        } catch (NoResultException nre) {
            return null;
        }
    }
}
