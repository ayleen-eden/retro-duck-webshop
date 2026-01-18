package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Userx;
import java.util.List;
import java.util.Optional;
/**
 * Repository for managing {@link Rating} entities.
 */
public interface RatingRepository extends AbstractRepository<Rating, Long> {
    boolean existsRatingByAuthorAndProduct(Userx author, Product product);
    Optional<Rating> findRatingByProduct_IdAndId(Long productId, Long ratingId);
    Optional<Rating> findRatingByProduct_idAndAuthor_Id(Long productId, Long authorId);
    List<Rating> getAllByProduct_Id(Long productId);
}
