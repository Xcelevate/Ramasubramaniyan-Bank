package com.training.mybank.service;

import com.training.mybank.dao.AccountDAO;
import com.training.mybank.dao.UserDAO;
import com.training.mybank.entities.AccountEntity;
import com.training.mybank.entities.UserEntity;
import com.training.mybank.util.AccountNumberGenerator;
import com.training.mybank.util.PasswordUtil;

import javax.persistence.EntityManager;

public class RegistrationService {
    private final UserDAO userDAO;
    private final AccountDAO accountDAO;
    private final EntityManager em;

    public RegistrationService(UserDAO userDAO, AccountDAO accountDAO, EntityManager em) {
        this.userDAO = userDAO;
        this.accountDAO = accountDAO;
        this.em = em;
    }

    public String register(String username, String password, String fullName, String email){
        if(userDAO.findByUsername(username)!=null){
            return null;
        }
        UserEntity user=new UserEntity();
        user.setUsername(username);
        user.setPassword(PasswordUtil.hash(password));
        user.setFullName(fullName);
        user.setEmail(email);
        userDAO.save(user);
        if(!accountDAO.existsByAccountNumber(username)){
            String accountNumner= AccountNumberGenerator.generate(em);
            AccountEntity account=new AccountEntity();
            account.setUsername(username);
            account.setAccountNumber(accountNumner);
            accountDAO.save(account);
            return accountNumner;
        }
        return null;
    }
}
