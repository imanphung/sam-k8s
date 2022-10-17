package com.samhello.sam.domain;

import com.samhello.sam.domain.enumeration.TypeOfSocial;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SocialNetWork.
 */
@Entity
@Table(name = "social_net_work")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class SocialNetWork implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "teacher_id", nullable = false)
    private Long teacherId;

    @NotNull
    @Column(name = "access_token", nullable = false)
    private String accessToken;

    @NotNull
    @Column(name = "expired_time", nullable = false)
    private Instant expiredTime;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private TypeOfSocial type;

    @NotNull
    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @NotNull
    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SocialNetWork id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTeacherId() {
        return this.teacherId;
    }

    public SocialNetWork teacherId(Long teacherId) {
        this.setTeacherId(teacherId);
        return this;
    }

    public void setTeacherId(Long teacherId) {
        this.teacherId = teacherId;
    }

    public String getAccessToken() {
        return this.accessToken;
    }

    public SocialNetWork accessToken(String accessToken) {
        this.setAccessToken(accessToken);
        return this;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Instant getExpiredTime() {
        return this.expiredTime;
    }

    public SocialNetWork expiredTime(Instant expiredTime) {
        this.setExpiredTime(expiredTime);
        return this;
    }

    public void setExpiredTime(Instant expiredTime) {
        this.expiredTime = expiredTime;
    }

    public TypeOfSocial getType() {
        return this.type;
    }

    public SocialNetWork type(TypeOfSocial type) {
        this.setType(type);
        return this;
    }

    public void setType(TypeOfSocial type) {
        this.type = type;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public SocialNetWork createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public SocialNetWork updatedAt(Instant updatedAt) {
        this.setUpdatedAt(updatedAt);
        return this;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SocialNetWork)) {
            return false;
        }
        return id != null && id.equals(((SocialNetWork) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SocialNetWork{" +
            "id=" + getId() +
            ", teacherId=" + getTeacherId() +
            ", accessToken='" + getAccessToken() + "'" +
            ", expiredTime='" + getExpiredTime() + "'" +
            ", type='" + getType() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
