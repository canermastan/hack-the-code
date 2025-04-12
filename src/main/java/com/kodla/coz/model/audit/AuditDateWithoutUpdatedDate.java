package com.kodla.coz.model.audit;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditDateWithoutUpdatedDate<T> {

    public AuditDateWithoutUpdatedDate(){

    }

    public AuditDateWithoutUpdatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    @CreatedDate
    @Column(name = "created_date", updatable = false)
    protected Date createdDate;

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }
}
