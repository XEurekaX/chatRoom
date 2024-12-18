package com.chatRoom.service.impl;


import com.chatRoom.dao.LoginInfoDAO;
import com.chatRoom.dao.MessageRecordDAO;
import com.chatRoom.dao.UserDAO;
import com.chatRoom.model.LoginInfoDo;
import com.chatRoom.model.MessageRecordDo;
import com.chatRoom.model.MessageRepository;
import com.chatRoom.model.User;
import com.chatRoom.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Scanner;

/**
 * 用户service实现类
 */
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private LoginInfoDAO loginInfoDAO;
    @Autowired
    private MessageRecordDAO messageRecordDAO;
    @Autowired
    private MessageRepository messageRepository;

    public User validateUserPassword(String name, String password) {
        return userDAO.queryUser(name, password);
    }

    public boolean isExistUser(String name) {
        User user = userDAO.queryUserByName(name);
        return user != null;
    }

    public void insertUser(String name, String password) {
        userDAO.insertUser(name, password);
    }

    @Override
    public void addUserLoginInfo(LoginInfoDo loginInfoDo) {
        loginInfoDAO.addLoginInfo(loginInfoDo);
    }

    @Override
    public void addUserMessageRecord(MessageRecordDo messageRecordDo) {
        messageRecordDAO.addMessageRecord(messageRecordDo);
    }

    @Override
    public User getUserByName(String name) {
        return userDAO.getUserByName(name);
    }


    public List<MessageRecordDo> getHistoryMessages() {
        return messageRepository.findAllByOrderByCreateTimeAsc();
    }
}
