package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.RatingAlreadyExistsException;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Optional;

@Service
public class RatingService {
    private final RatingRepository ratingRepository;

    @Autowired
    public RatingService(RatingRepository ratingRepository) {
        this.ratingRepository = ratingRepository;
    }

    /**
     * Returns a collection of all ratings for every product on the site.
     * Can be useful for a potential Manager view
     *
     * @return the rating collection
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Rating> getAllRatings() { return ratingRepository.findAll(); }

    /**
     * Returns a collection of all ratings for a certain product.
     *
     * @param productId productId where the ratings are stored
     * @return the rating collection for a certain product
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Rating> getAllRatingsByProduct(Long productId) {
        return ratingRepository.getAllByProduct_Id(productId);
    }

    /**
     * Saves the Rating. This method will also set {@link Rating#timestamp} for new
     * and updated entities.
     *
     * @param rating Rating to save
     * @return the updated Rating
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Rating saveRating(Rating rating) {
        if (rating.isNew()) {
            if (ratingRepository.existsRatingByAuthorAndProduct(rating.getAuthor(), rating.getProduct())) {
                throw new RatingAlreadyExistsException("User " + rating.getUsername() + "already submitted a rating");
            }
        } else {
            rating.setTimestamp(LocalDateTime.now());
        }
        return ratingRepository.save(rating);
    }

    /**
     * Deletes the rating.
     *
     * @param rating the rating to delete
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void deleteRating(Rating rating) {
        Optional<Rating> ratingOpt = ratingRepository.findById(rating.getId());
        ratingOpt.ifPresent(ratingRepository::delete);
    }

    /**
     * Loads a rating by its productId and ratingId.
     * This uses the productId to be inline with the API specification.
     *
     * @param productId productId where the ratings are stored
     * @param ratingId global ratingId
     * @return the rating for the product with the id
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Optional<Rating> loadRating(Long productId, Long ratingId) {
        return ratingRepository.findRatingByProduct_IdAndId(productId, ratingId);
    }

    /**
     * Loads a rating by its productId and authorId. Preferred method.
     * This uses the productId to be inline with the API specification.
     *
     * @param productId productId where the ratings are stored
     * @param authorId global authorId
     * @return the rating for the product from the author
     */
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Optional<Rating> loadRatingByAuthor(Long productId, Long authorId) {
        return ratingRepository.findRatingByProduct_idAndAuthor_Id(productId, authorId);
    }
}