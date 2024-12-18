package com.chatRoom.model;

import com.chatRoom.model.MessageRecordDo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<MessageRecordDo, Long> {

    /**
     * 查询所有历史消息，按时间升序排列
     * @return 历史消息列表
     */
    List<MessageRecordDo> findAllByOrderByCreateTimeAsc();
}
