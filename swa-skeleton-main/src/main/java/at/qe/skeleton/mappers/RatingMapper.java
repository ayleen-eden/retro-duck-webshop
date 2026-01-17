package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.RatingDTO;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.RatingService;
import at.qe.skeleton.services.UserxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Mapping between RatingTypes and RatingDTOs.
 */
@Service
public class RatingMapper implements DTOMapper<Rating, RatingDTO>{
    private final RatingService ratingService;
    private final ProductService productService;
    private final UserxService userxService;

    @Autowired
    public RatingMapper(RatingService ratingService, ProductService productService, UserxService userxService) {
        this.ratingService = ratingService;
        this.productService = productService;
        this.userxService = userxService;
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
        Product product = productService.getProductById(ratingDto.productId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Userx author = userxService.loadUser(ratingDto.authorId()).orElseThrow(() -> new RuntimeException("Author (user) not found"));

        rating.setRating(ratingDto.rating());
        rating.setComment(ratingDto.comment());
        rating.setProduct(product);
        rating.setAuthor(author);

        return rating;
    }
}
