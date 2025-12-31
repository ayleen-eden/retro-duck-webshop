package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.RatingAlreadyExistsException;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Userx;
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
    private final AuthenticatedUserService authenticatedUserService;

    @Autowired
    public RatingService(RatingRepository ratingRepository, AuthenticatedUserService authenticatedUserService) {
        this.ratingRepository = ratingRepository;
        this.authenticatedUserService = authenticatedUserService;
    }

    //Can be used for a manager view. Otherwise used for testing
    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Rating> getAllRatings() { return ratingRepository.findAll(); }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Rating saveRating(Rating rating) {
        if (rating.isNew()) {
            if (ratingRepository.existsRatingByAuthorAndProduct(rating.getAuthor(), rating.getProduct())) {
                throw new RatingAlreadyExistsException("User " + rating.getAuthor().getUsername() + "already submitted a rating");
            }
        } else {
            rating.setTimestamp(LocalDateTime.now());
        }
        return ratingRepository.save(rating);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public void deleteRating(Rating rating) {
        Optional<Rating> ratingOpt = ratingRepository.findById(rating.getId());
        ratingOpt.ifPresent(ratingRepository::delete);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Optional<Rating> loadRating(Long productId, Long ratingId) {
        return ratingRepository.findRatingByProduct_IdAndId(productId, ratingId);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Optional<Rating> loadRatingByAuthor(Long productId, Long authorId) {
        return ratingRepository.findRatingByProduct_idAndAuthor_Id(productId, authorId);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Rating> getAllRatingsByProduct(Product product) {
        return ratingRepository.getAllByProduct(product);
    }
}


