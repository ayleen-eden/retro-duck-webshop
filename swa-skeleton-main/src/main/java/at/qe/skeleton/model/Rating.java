package at.qe.skeleton.model;

import jakarta.persistence.*;
import jdk.jfr.Timestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
public class Rating implements Persistable<Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Timestamp
    private LocalDateTime timestamp;
    private RatingScale rating;
    @Column(unique = true, nullable = false, length = 50000)
    private String comment;
    @OneToOne
    private Userx author;
    //TODO: Add products @ManyToOne

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public RatingScale getRating() {
        return rating;
    }

    public void setRating(RatingScale rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Userx getAuthor() {
        return author;
    }

    public void setAuthor(Userx author) {
        this.author = author;
    }

    @Override
    public String toString() {
        return "%s\n Rated %s Stars\n Written by User %s at %s\n".formatted(
                comment,
                author.getUsername(),
                timestamp.toString(),
                rating.toString());
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public boolean isNew() {
        return (null == id);
    }
}
