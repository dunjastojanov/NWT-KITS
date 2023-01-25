package com.uber.rocket.repository;

import com.uber.rocket.entity.user.Message;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message, Long> {

    @Query("SELECT message FROM Message message WHERE (message.receiver = :receiver and message.sender=:sender) or (message.receiver = :sender and message.sender=:receiver) order by message.sentAt asc")
    List<Message> findAllByReceiverIsAndSenderIsOrderBySentAtAsc(@Param("receiver") User receiver, @Param("sender") User sender);

    @Query("SELECT DISTINCT message.sender FROM Message message WHERE message.receiver = :receiver")
    List<User> findAllDistinctSendersByReceiver(@Param("receiver") User receiver);


}
