package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Userx;

import java.util.Optional;

public interface RatingRepository extends AbstractRepository<Rating, Long> {
    boolean existsRatingByAuthor(Userx author);
    Optional<Rating> findRatingByAuthor(Userx author);
}
