package com.tanle.e_commerce.dao;



import com.tanle.e_commerce.entities.User;

import java.util.List;


public  interface UserDAO {
    List<User> findAllUser();
    User findById(Integer id);
    void delete(Integer id);
    User update(User user);

}

