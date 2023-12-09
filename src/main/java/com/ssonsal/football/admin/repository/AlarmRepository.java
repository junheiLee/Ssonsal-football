package com.ssonsal.football.admin.repository;

import com.ssonsal.football.admin.entity.Alarm;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AlarmRepository extends JpaRepository<Alarm,Long> {
    Optional<Alarm> findByTopicArnAndSubscriptionArn(String topicArn, String subscriptionArn);
}
