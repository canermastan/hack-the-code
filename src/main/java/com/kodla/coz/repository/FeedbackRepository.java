package com.kodla.coz.repository;

import com.kodla.coz.model.Feedback;
import com.kodla.coz.utility.Utilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.transaction.Transactional;

@Repository
@Transactional
public class FeedbackRepository {
    public FeedbackRepository(EntityManager entityManager, Utilities utilities) {
        this.entityManager = entityManager;
        this.utilities = utilities;
    }

    private final Logger logger = LoggerFactory.getLogger(FeedbackRepository.class);
    private final EntityManager entityManager;
    private final Utilities utilities;

    public void saveFeedback(Feedback feedback) {
        entityManager.createNativeQuery("INSERT INTO feedbacks (email, feedback_type, message, url, created_date) VALUES (?, ?, ?, ?, ?)")
                .setParameter(1, feedback.getEmail())
                .setParameter(2, feedback.getFeedbackType())
                .setParameter(3, feedback.getMessage())
                .setParameter(4, feedback.getUrl())
                .setParameter(5, feedback.getCreatedDate())
                .executeUpdate();
        try {
            utilities.sendFeedbackMail(feedback);
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}
