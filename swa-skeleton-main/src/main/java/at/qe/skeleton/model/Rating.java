package at.qe.skeleton.model;

import jakarta.persistence.*;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.domain.Persistable;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * Entity representing ratings.
 */
@Entity
public class Rating implements Persistable<Long>, Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @UpdateTimestamp
    private LocalDateTime timestamp;
    @Enumerated(EnumType.STRING)
    private RatingScale ratingScale;
    @Column(nullable = false, length = 50000)
    private String comment;
    @ManyToOne(fetch = FetchType.LAZY)
    private Userx author;
    @Transient
    private String username;
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public RatingScale getRatingScale() {
        return ratingScale;
    }

    public void setRatingScale(RatingScale ratingScale) {
        this.ratingScale = ratingScale;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    @Override
    public String toString() {
        return "%s\n Rated %s Stars\n Written by User %s at %s\n".formatted(
                comment,
                author.getUsername(),
                timestamp.toString(),
                ratingScale.toString());
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
