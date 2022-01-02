package com.epam.esm.model.audit;

import com.epam.esm.model.entity.ApplicationBaseEntity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.persistence.PostPersist;
import javax.persistence.PostRemove;
import javax.persistence.PostUpdate;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.PreUpdate;

public class EntityAuditListener {

    private final Logger logger = LoggerFactory.getLogger(EntityAuditListener.class);

    @PrePersist
    private void prePersistAudit(ApplicationBaseEntity entity) {
        logger.info("Persisting an entity: " + entity);
    }

    @PostPersist
    private void postPersistAudit(ApplicationBaseEntity entity) {
        logger.info("The entity persisted: " + entity + " with id=" + entity.getId());
    }

    @PreUpdate
    private void preUpdateAudit(ApplicationBaseEntity entity) {
        logger.info("Updating an entity: " + entity);
    }

    @PostUpdate
    private void postUpdateAudit(ApplicationBaseEntity entity) {
        logger.info("The entity updated: " + entity);
    }

    @PreRemove
    private void preRemoveAudit(ApplicationBaseEntity entity) {
        logger.info("Removing an entity: " + entity);
    }

    @PostRemove
    private void postRemoveAudit(ApplicationBaseEntity entity) {
        logger.info("The entity removed: " + entity);
    }
}
