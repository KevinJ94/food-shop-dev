package com.shop.service;

import com.shop.pojo.Users;
import com.shop.pojo.bo.UserBO;

public interface UserService {

    public  boolean queryUsernameIsExist(String username);

    public Users createUser(UserBO userBO);

    public Users queryUserForLogin(String username, String password);
}
