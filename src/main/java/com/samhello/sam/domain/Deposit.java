package com.samhello.sam.domain;

import com.samhello.sam.domain.enumeration.DepositStatus;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Deposit.
 */
@Entity
@Table(name = "deposit")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Deposit implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "student_id", nullable = false)
    private Long studentId;

    @NotNull
    @Column(name = "transfer_id", nullable = false)
    private Long transferId;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private DepositStatus status;

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

    public Deposit id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public Deposit studentId(Long studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getTransferId() {
        return this.transferId;
    }

    public Deposit transferId(Long transferId) {
        this.setTransferId(transferId);
        return this;
    }

    public void setTransferId(Long transferId) {
        this.transferId = transferId;
    }

    public DepositStatus getStatus() {
        return this.status;
    }

    public Deposit status(DepositStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(DepositStatus status) {
        this.status = status;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Deposit createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Deposit updatedAt(Instant updatedAt) {
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
        if (!(o instanceof Deposit)) {
            return false;
        }
        return id != null && id.equals(((Deposit) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Deposit{" +
            "id=" + getId() +
            ", studentId=" + getStudentId() +
            ", transferId=" + getTransferId() +
            ", status='" + getStatus() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
