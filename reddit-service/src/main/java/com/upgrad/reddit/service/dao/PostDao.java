package com.upgrad.reddit.service.dao;

import com.upgrad.reddit.service.entity.PostEntity;
import com.upgrad.reddit.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

/**
 * PostDao class provides the database access for all the endpoints in post controller.
 */
@Repository
public class PostDao {
    @PersistenceContext
    private EntityManager entityManager;

    public PostEntity createPost(PostEntity postEntity) {
        entityManager.persist(postEntity);
        return postEntity;
    }


    public TypedQuery<PostEntity> getPosts() {
        try {
            return entityManager.createNamedQuery("getAllPosts", PostEntity.class);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public PostEntity getPostByUuid(String Uuid) {
        try {
            return entityManager.createNamedQuery("postByUuid", PostEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }

    public PostEntity editPost(PostEntity postEntity) {
        return entityManager.merge(postEntity);
    }

    public PostEntity deletePost(PostEntity postEntity) {
        entityManager.remove(postEntity);
        return postEntity;
    }

    public TypedQuery<PostEntity> getPostsByUser(UserEntity userEntity) {
        try {
            return entityManager.createNamedQuery("getAllPostsByUser", PostEntity.class).setParameter("user", userEntity);
        } catch (NoResultException nre) {
            return null;
        }
    }

    public UserEntity getUserByUuid(String Uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
