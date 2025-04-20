package com.tanle.e_commerce.dao;


import com.tanle.e_commerce.entities.MyUser;
import jakarta.persistence.EntityManager;
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
    public List<MyUser> findAllUser() {
        List<MyUser> myUsers = entityManager.createQuery("from MyUser ", MyUser.class).getResultList();
        return myUsers;
    }
    @Override
    public MyUser findById(Integer id) {
        MyUser myUser =  entityManager.find(MyUser.class,id);
        return myUser;
    }
    @Override
    public void delete(Integer id) {
       MyUser myUser = findById(id);
       if(myUser != null)
           entityManager.remove(myUser);
    }
    @Override
    public MyUser update(MyUser myUser) {
       MyUser result = entityManager.merge(myUser);
       return result;
    }
}
