package com.devlog.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author sbsong
 * @package com.devlog.domain
 * @Classname BaseEntity.java
 * @Description ""
 * <PRE>
 * ---------------------------------
 * 개정이력
 * 2025-07-15 sbsong : 최초작성
 * </PRE>
 * @since 2025-07-15
 */
@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseEntity {
    @CreatedDate
    @Column(updatable = false)
    protected LocalDateTime createdAt;

    @LastModifiedDate
    protected LocalDateTime updatedAt;

    @Column(nullable = false)
    protected Boolean isDeleted = false;

    protected LocalDateTime deletedAt;

    public void softDeleted() {
        this.isDeleted = true;
        this.deletedAt = LocalDateTime.now();
    }

    public boolean isNotDeleted(){
        return !Boolean.TRUE.equals(this.isDeleted);
    }
}
