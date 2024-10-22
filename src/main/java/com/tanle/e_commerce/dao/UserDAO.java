package com.tanle.e_commerce.dao;



import com.tanle.e_commerce.entities.MyUser;

import java.util.List;


public  interface UserDAO {
    List<MyUser> findAllUser();
    MyUser findById(Integer id);
    void delete(Integer id);
    MyUser update(MyUser myUser);

}

