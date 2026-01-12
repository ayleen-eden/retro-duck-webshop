package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.RatingDTO;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.services.RatingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RatingMapper implements DTOMapper<Rating, RatingDTO>{
    private final RatingService ratingService;

    @Autowired
    public RatingMapper(RatingService ratingService) {
        this.ratingService = ratingService;
    }

    @Override
    public RatingDTO mapTo(Rating rating) {
        if (rating == null) {
            return null;
        }
        RatingDTO dto = new RatingDTO(
                rating.getId(),
                rating.getTimestamp(),
                rating.getRating(),
                rating.getComment(),
                rating.getAuthor().getId(),
                rating.getProduct().getId()
        );

        return dto;
    }

    @Override
    public Rating mapFrom(RatingDTO ratingDto) {
        if (null == ratingDto) {
            return null;
        }
        Rating rating;
        if (null != ratingDto.id()) {
            rating = ratingService.loadRating(ratingDto.productId(), ratingDto.id()).orElse(new Rating());
        } else {
            rating = new Rating();
        }
        rating.setRating(ratingDto.rating());
        rating.setComment(ratingDto.comment());
        //TODO: Check if productId and author needs to be assigned

        return rating;
    }
}
