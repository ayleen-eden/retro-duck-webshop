package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.RatingCreateDTO;
import at.qe.skeleton.model.Rating;
import org.springframework.stereotype.Service;

@Service
public class RatingCreateMapper implements DTOMapper<Rating, RatingCreateDTO>{
    @Override
    public RatingCreateDTO mapTo(Rating entity) {
        throw new UnsupportedOperationException("This doesn't need to be implemented");
    }

    @Override
    public Rating mapFrom(RatingCreateDTO dto) {
        Rating rating = new Rating();
        rating.setRating(dto.rating());
        rating.setComment(dto.comment());
        rating.setAuthor(dto.author());
        rating.setProduct(dto.product());

        return rating;
    }
}
