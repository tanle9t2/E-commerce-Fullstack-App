package com.tanle.e_commerce.dao;


import com.tanle.e_commerce.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaDelete;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class UserDAOImpl implements UserDAO{
    private EntityManager entityManager;
    @Autowired
    public UserDAOImpl (EntityManager entityManager) {
        this.entityManager = entityManager;
    }
    @Override
    public List<User> findAllUser() {
        List<User> users = entityManager.createQuery("from User ", User.class).getResultList();
        return users;
    }
    @Override
    public User findById(Integer id) {
        User user=  entityManager.find(User.class,id);
        return user;
    }
    @Override
    public void delete(Integer id) {
       User user = findById(id);
       if(user != null)
           entityManager.remove(user);
    }
    @Override
    public User update(User user) {
       User result = entityManager.merge(user);
       return result;
    }
}
