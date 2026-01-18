package at.qe.skeleton.mappers;

import at.qe.skeleton.dtos.RatingCreateDTO;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.UserxService;
import org.springframework.stereotype.Service;

/**
 * Mapping between RatingCreateDTO and RatingTypes.
 */
@Service
public class RatingCreateMapper implements DTOMapper<Rating, RatingCreateDTO>{
    private final ProductService productService;
    private final UserxService userxService;

    public RatingCreateMapper(ProductService productService, UserxService userxService) {
        this.productService = productService;
        this.userxService = userxService;
    }

    @Override
    public RatingCreateDTO mapTo(Rating entity) {
        throw new UnsupportedOperationException("This doesn't need to be implemented");
    }

    @Override
    public Rating mapFrom(RatingCreateDTO dto) {
        Rating rating = new Rating();
        rating.setRating(dto.rating());
        rating.setComment(dto.comment());

        Product product = productService.getProductById(dto.productId()).orElseThrow(() -> new RuntimeException("Product not found"));
        Userx author = userxService.loadUser(dto.authorId()).orElseThrow(() -> new RuntimeException("Author (user) not found"));
        rating.setAuthor(author);
        rating.setProduct(product);

        return rating;
    }
}
