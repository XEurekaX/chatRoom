package com.chatRoom.model;

import javax.persistence.*;
import java.util.Date;

@Entity // 标记为 JPA 实体
@Table(name = "message_record") // 指定数据库表名
public class MessageRecordDo {

    @Id // 标记主键
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 主键自增策略
    private Integer id; // 主键

    @Column(name = "user_id")
    private Integer userId; // 用户ID

    @Column(name = "user_name")
    private String userName; // 用户名

    @Column(name = "message_type")
    private Integer messageType; // 消息类型（1.文本，2.图片）

    @Column(name = "content")
    private String content; // 消息内容

    @Column(name = "create_time")
    private Date createTime; // 创建时间

    // 无参构造函数
    public MessageRecordDo() {
    }

    // 全参构造函数
    public MessageRecordDo(Integer id, Integer userId, String userName, Integer messageType, String content, Date createTime) {
        this.id = id;
        this.userId = userId;
        this.userName = userName;
        this.messageType = messageType;
        this.content = content;
        this.createTime = createTime;
    }

    // 构建器模式
    public static MessageRecordBuilder messageRecordBuilder() {
        return new MessageRecordBuilder();
    }

    // 构建器类
    public static class MessageRecordBuilder {
        private Integer id;
        private Integer userId;
        private String userName;
        private Integer messageType;
        private String content;
        private Date createTime;

        public MessageRecordBuilder id(Integer id) {
            this.id = id;
            return this;
        }

        public MessageRecordBuilder userId(Integer userId) {
            this.userId = userId;
            return this;
        }

        public MessageRecordBuilder userName(String userName) {
            this.userName = userName;
            return this;
        }

        public MessageRecordBuilder messageType(Integer messageType) {
            this.messageType = messageType;
            return this;
        }

        public MessageRecordBuilder content(String content) {
            this.content = content;
            return this;
        }

        public MessageRecordBuilder createTime(Date createTime) {
            this.createTime = createTime;
            return this;
        }

        public MessageRecordDo build() {
            return new MessageRecordDo(id, userId, userName, messageType, content, createTime);
        }
    }

    // Getters and Setters
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getMessageType() {
        return messageType;
    }

    public void setMessageType(Integer messageType) {
        this.messageType = messageType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}