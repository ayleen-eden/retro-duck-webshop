package at.qe.skeleton.services;

import at.qe.skeleton.exceptions.RatingAlreadyExistsException;
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

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Collection<Rating> getAllRatings() { return ratingRepository.findAll(); }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    public Rating saveRating(Rating rating) {
        if (rating.isNew()) {
            if (ratingRepository.existsRatingByAuthor(rating.getAuthor())) {
                throw new RatingAlreadyExistsException("User " + rating.getAuthor().getUsername() + "already submitted a rating");
            }
            rating.setAuthor(authenticatedUserService.getAuthenticatedUser());
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
    public Optional<Rating> loadRating(Long id) {
        return ratingRepository.findById(id);
    }

    @PreAuthorize("hasAuthority('CUSTOMER')")
    //TODO: Rating by product and author!
    public Optional<Rating> loadRating(Userx author) {
        return ratingRepository.findRatingByAuthor(author);
    }
}


