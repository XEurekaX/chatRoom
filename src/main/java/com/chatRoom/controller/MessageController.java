package com.chatRoom.controller;

import com.chatRoom.enums.MessageTypeEnum;
import com.chatRoom.model.Message;
import com.chatRoom.model.MessageRecordDo;
import com.chatRoom.model.User;
import com.chatRoom.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * 发送消息控制器
 */
@Controller
public class MessageController {
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    private static final String SUBSCRIBE_MESSAGE_URI = "/topic/chat/message"; //订阅接收消息地址

    private static final String IMAGE_PREFIX = "/upload/";  //服务器储存上传图片地址的前缀

    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    /**
     * 接收并且转发消息
     * @param message
     */
    @MessageMapping("/chat/message")
    public void receiveMessage(Message message){
        message.setSendDate(new Date());
        message.setMessageType("text");
        logger.info(message.getSendDate() + "," + message.getUserName() + " send a message:" + message.getContent());
        //保存聊天信息
        User userByName = userService.getUserByName(message.getUserName());
        MessageRecordDo messageRecordDo = MessageRecordDo.messageRecordBuilder()
                .userId(userByName == null ? null : userByName.getId())
                .userName(message.getUserName()).content(message.getContent())
                .messageType(MessageTypeEnum.TEXT.getCode()).createTime(new Date()).build();
        userService.addUserMessageRecord(messageRecordDo);
        messagingTemplate.convertAndSend(SUBSCRIBE_MESSAGE_URI, message);
    }

    /**
     * 接收转发图片
     * @param request
     * @param imageFile
     * @param userName
     * @return
     */
    @RequestMapping(value = "/upload/image", method = RequestMethod.POST)
    @ResponseBody
    public String handleUploadImage(HttpServletRequest request, @RequestParam("image")MultipartFile imageFile,
                                    @RequestParam("userName")String userName, Model model){
        if (!imageFile.isEmpty()){
            String imageName = userName + "_" + UUID.randomUUID().toString() + ".jpg";
            File file = new File(request.getSession().getServletContext().getRealPath(IMAGE_PREFIX) + "/" + imageName);
            if (!file.exists()){
                file.mkdirs();
            }
            try {
                //上传图片到目录
                imageFile.transferTo(file);

                Message message = new Message();
                message.setMessageType("image");
                message.setUserName(userName);
                message.setSendDate(new Date());
                // 图片的src
                message.setContent(request.getContextPath() + IMAGE_PREFIX + imageName);

                //保存发送图片信息
                User userByName = userService.getUserByName(message.getUserName());
                MessageRecordDo messageRecordDo = MessageRecordDo.messageRecordBuilder()
                        .userId(userByName == null ? null : userByName.getId())
                        .userName(userName).content(message.getContent())
                        .messageType(MessageTypeEnum.IMAGE.getCode()).createTime(new Date()).build();
                userService.addUserMessageRecord(messageRecordDo);

                messagingTemplate.convertAndSend(SUBSCRIBE_MESSAGE_URI, message);
            } catch (IOException e) {
                logger.error("图片上传失败：" + e.getMessage(), e);
                return "upload false";
            }
        }
        return "upload success";
    }

    /**
     * 获取历史消息
     * @return 历史消息列表
     */
    @RequestMapping(value = "/api/chat/history", method = RequestMethod.GET)
    @ResponseBody
    public List<MessageRecordDo> getHistoryMessages() {
        // 从数据库中获取历史消息
        return userService.getHistoryMessages();
    }
}