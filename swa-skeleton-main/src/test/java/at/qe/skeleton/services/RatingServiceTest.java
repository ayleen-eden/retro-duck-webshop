//package at.qe.skeleton.services;
//
//import at.qe.skeleton.exceptions.RatingAlreadyExistsException;
//import at.qe.skeleton.model.*;
//import org.junit.Ignore;
//import org.junit.jupiter.api.Assertions;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.test.context.support.WithMockUser;
//import org.springframework.test.annotation.DirtiesContext;
//
//import java.util.HashSet;
//import java.util.Optional;
//import java.util.Set;
//
//@SpringBootTest()
//public class RatingServiceTest {
//    @Autowired
//    RatingService ratingService;
//    @Autowired
//    ProductService productService;
//    @Autowired
//    AuthenticatedUserService authenticatedUserService;
//
//    private Product product1;
//    private Product product2;
//
//    @BeforeEach
//    public void setup() {
//        product1 = new Product();
//        product2 = new Product();
//        Set<ProductCategory> productCategory = new HashSet<>();
//
//        productCategory.add(ProductCategory.NES);
//        product1.setName("Duck Hunt");
//        product1.setCategories(productCategory);
//        product1.setPrice(0.0);
//        product1.setDiscount(0.0);
//        product1.setDescription("Test");
//
//        product2.setName("Super Mario Bros.");
//        product2.setCategories(productCategory);
//        product2.setPrice(0.0);
//        product2.setDiscount(0.0);
//        product2.setDescription("Test");
//
//        productService.saveProduct(product1);
//        productService.saveProduct(product2);
//    }
//
//    @Ignore
//    @DirtiesContext
//    @Test
//    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
//    public void testCreateRating() {
//        Userx author = authenticatedUserService.getAuthenticatedUser();
//
//        Rating ratingToInsert1 = new Rating();
//        RatingScale ratingScale1 = RatingScale.FOUR_STARS;
//        String ratingComment1 = "This is a rating of a productId";
//        ratingToInsert1.setRating(ratingScale1);
//        ratingToInsert1.setAuthor(author);
//        ratingToInsert1.setComment(ratingComment1);
//        ratingToInsert1.setProduct(product1);
//        Rating savedRating = ratingService.saveRating(ratingToInsert1);
//
//        Rating ratingToInsert2 = new Rating();
//        RatingScale ratingScale2 = RatingScale.FOUR_STARS;
//        String ratingComment2 = "This is a rating of a productId";
//        ratingToInsert2.setRating(ratingScale2);
//        ratingToInsert2.setAuthor(author);
//        ratingToInsert2.setComment(ratingComment2);
//        ratingToInsert2.setProduct(product2);
//        ratingService.saveRating(ratingToInsert2);
//
//        Assertions.assertEquals(2, ratingService.getAllRatings().size());
//        Optional<Rating> freshlyCreatedRatingOpt = ratingService.loadRating(savedRating.getId(), product1.getId());
//        Assertions.assertFalse(freshlyCreatedRatingOpt.isEmpty(),
//                "New rating could not be loaded from test data source after being saved");
//        Rating freshlyCreatedRating = freshlyCreatedRatingOpt.get();
//        Assertions.assertEquals(ratingScale1, freshlyCreatedRating.getRating());
//        Assertions.assertEquals(author, freshlyCreatedRating.getAuthor());
//        Assertions.assertEquals(ratingComment1, freshlyCreatedRating.getComment());
//    }
//
//    @DirtiesContext
//    @Test
//    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
//    public void testUpdateRating() {
//        Userx author = authenticatedUserService.getAuthenticatedUser();
//
//        Rating rating = new Rating();
//        rating.setAuthor(author);
//        rating.setProduct(product1);
//        rating.setRating(RatingScale.FOUR_STARS);
//        rating.setComment("Kinda peak");
//        ratingService.saveRating(rating);
//
//        Optional<Rating> toBeChangedRatingOpt = ratingService.loadRatingByAuthor(product1.getId(), author.getId());
//        Assertions.assertNotNull(toBeChangedRatingOpt, "Rating could not be loaded from repository");
//
//        Rating toBeChangedRating = toBeChangedRatingOpt.get();
//
//        toBeChangedRating.setRating(RatingScale.FIVE_STARS);
//        String updatedRatingComment = "I changed my mind. This product is awesome. Absolute Cinema!";
//        toBeChangedRating.setComment(updatedRatingComment);
//
//        ratingService.saveRating(toBeChangedRating);
//
//        Optional<Rating> freshlyUpdatedRatingOpt = ratingService.loadRatingByAuthor(product1.getId(), author.getId());
//        Assertions.assertFalse(freshlyUpdatedRatingOpt.isEmpty(), "Updated Rating could not be loaded");
//        Rating freshlyUpdatedRating = freshlyUpdatedRatingOpt.get();
//        Assertions.assertEquals(RatingScale.FIVE_STARS, freshlyUpdatedRating.getRating());
//        Assertions.assertEquals(updatedRatingComment, freshlyUpdatedRating.getComment());
//    }
//
//    @DirtiesContext
//    @Test
//    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
//    public void testDuplicateRatingException() {
//        Userx author = authenticatedUserService.getAuthenticatedUser();
//
//        Rating firstRating = new Rating();
//        RatingScale ratingScale = RatingScale.ONE_STAR;
//        String ratingComment = "It sucks!";
//        firstRating.setRating(ratingScale);
//        firstRating.setAuthor(author);
//        firstRating.setComment(ratingComment);
//        firstRating.setProduct(product1);
//        ratingService.saveRating(firstRating);
//
//        Rating ratingToInsert = new Rating();
//        ratingComment = "Reviewbomb incoming";
//        ratingToInsert.setRating(ratingScale);
//        ratingToInsert.setAuthor(author);
//        ratingToInsert.setComment(ratingComment);
//        ratingToInsert.setProduct(product1);
//
//        Assertions.assertThrows(RatingAlreadyExistsException.class, () -> ratingService.saveRating(ratingToInsert));
//        Assertions.assertEquals(1, ratingService.getAllRatings().size());
//    }
//
//    @DirtiesContext
//    @Test
//    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
//    public void testDeleteRating() {
//        while (!ratingService.getAllRatings().isEmpty()) {
//            ratingService.deleteRating(ratingService.getAllRatings().iterator().next());
//        }
//        Assertions.assertTrue(ratingService.getAllRatings().isEmpty());
//    }
//}
