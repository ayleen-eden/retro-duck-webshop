package at.qe.skeleton.tests;

import at.qe.skeleton.exceptions.RatingAlreadyExistsException;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.model.RatingScale;
import at.qe.skeleton.model.Userx;
import at.qe.skeleton.repositories.UserxRepository;
import at.qe.skeleton.services.AuthenticatedUserService;
import at.qe.skeleton.services.AuthenticationService;
import at.qe.skeleton.services.RatingService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Optional;

@SpringBootTest()
public class RatingServiceTest {
    @Autowired
    RatingService ratingService;
    @Autowired
    AuthenticatedUserService authenticatedUserService;


    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testCreateRating() {
        Userx author = authenticatedUserService.getAuthenticatedUser();
        Rating ratingToInsert = new Rating();
        RatingScale ratingScale = RatingScale.FOUR_STARS;
        String ratingComment = "This is a rating of a product";
        ratingToInsert.setRating(ratingScale);
        ratingToInsert.setAuthor(author);
        ratingToInsert.setComment(ratingComment);
        Rating savedRating = ratingService.saveRating(ratingToInsert);
        Assertions.assertEquals(1, ratingService.getAllRatings().size());
        Optional<Rating> freshlyCreatedRatingOpt = ratingService.loadRating(savedRating.getId());
        Assertions.assertFalse(freshlyCreatedRatingOpt.isEmpty(),
                "New rating could not be loaded from test data source after being saved");
        Rating freshlyCreatedRating = freshlyCreatedRatingOpt.get();
        Assertions.assertEquals(ratingScale, freshlyCreatedRating.getRating());
        Assertions.assertEquals(author, freshlyCreatedRating.getAuthor());
        Assertions.assertEquals(ratingComment, freshlyCreatedRating.getComment());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testDuplicateRatingException() {
        Userx author = authenticatedUserService.getAuthenticatedUser();
        Rating ratingToInsert = new Rating();
        RatingScale ratingScale = RatingScale.ONE_STAR;
        String ratingComment = "Reviewbomb incoming";
        ratingToInsert.setAuthor(author);
        ratingToInsert.setRating(ratingScale);
        ratingToInsert.setComment(ratingComment);

        Assertions.assertThrows(RatingAlreadyExistsException.class, () -> ratingService.saveRating(ratingToInsert));
        Assertions.assertEquals(1, ratingService.getAllRatings().size());
    }

    @Test
    @WithMockUser(username = "user1", authorities = {"CUSTOMER"})
    public void testDeleteRating() {
        Rating lastRating = ratingService.getAllRatings().iterator().next();
        ratingService.deleteRating(lastRating);
        Assertions.assertTrue(ratingService.getAllRatings().isEmpty());
    }


}
