package com.samhello.sam.domain;

import com.samhello.sam.domain.enumeration.TypeOfStar;
import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;
import javax.validation.constraints.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Report.
 */
@Entity
@Table(name = "report")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Report implements Serializable {

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
    @Column(name = "lesson_id", nullable = false)
    private Long lessonId;

    @Column(name = "comment")
    private String comment;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "star", nullable = false)
    private TypeOfStar star;

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

    public Report id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getStudentId() {
        return this.studentId;
    }

    public Report studentId(Long studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(Long studentId) {
        this.studentId = studentId;
    }

    public Long getLessonId() {
        return this.lessonId;
    }

    public Report lessonId(Long lessonId) {
        this.setLessonId(lessonId);
        return this;
    }

    public void setLessonId(Long lessonId) {
        this.lessonId = lessonId;
    }

    public String getComment() {
        return this.comment;
    }

    public Report comment(String comment) {
        this.setComment(comment);
        return this;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public TypeOfStar getStar() {
        return this.star;
    }

    public Report star(TypeOfStar star) {
        this.setStar(star);
        return this;
    }

    public void setStar(TypeOfStar star) {
        this.star = star;
    }

    public Instant getCreatedAt() {
        return this.createdAt;
    }

    public Report createdAt(Instant createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return this.updatedAt;
    }

    public Report updatedAt(Instant updatedAt) {
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
        if (!(o instanceof Report)) {
            return false;
        }
        return id != null && id.equals(((Report) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Report{" +
            "id=" + getId() +
            ", studentId=" + getStudentId() +
            ", lessonId=" + getLessonId() +
            ", comment='" + getComment() + "'" +
            ", star='" + getStar() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", updatedAt='" + getUpdatedAt() + "'" +
            "}";
    }
}
