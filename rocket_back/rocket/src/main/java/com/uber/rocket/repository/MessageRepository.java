package com.uber.rocket.repository;

import com.uber.rocket.entity.user.Message;
import com.uber.rocket.entity.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MessageRepository extends JpaRepository<Message,Long> {

    List<Message> findAllByReceiverIsAndSenderIsOrderBySentAtAsc(User receiver, User sender);

    List<User> findDistinctSendersByReceiverIs(User receiver);

}
