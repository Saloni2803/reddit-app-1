package com.upgrad.reddit.service.dao;

import com.upgrad.reddit.service.entity.UserEntity;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

/**
 * CommonDao class provides the database access for all the endpoints in common controller.
 */
@Repository
public class CommonDao {

    @PersistenceContext
    private EntityManager entityManager;

    public UserEntity getUserByUuid(String Uuid) {
        try {
            return entityManager.createNamedQuery("userByUuid", UserEntity.class).setParameter("uuid", Uuid).getSingleResult();
        } catch (NoResultException nre) {
            return null;
        }
    }
}
