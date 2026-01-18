package at.qe.skeleton.controllers;

import at.qe.skeleton.dtos.RatingCreateDTO;
import at.qe.skeleton.dtos.RatingDTO;
import at.qe.skeleton.mappers.RatingCreateMapper;
import at.qe.skeleton.mappers.RatingMapper;
import at.qe.skeleton.model.Product;
import at.qe.skeleton.model.Rating;
import at.qe.skeleton.services.ProductService;
import at.qe.skeleton.services.RatingService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * Rating endpoints exposed by the server.
 */
@RestController
@RequestMapping("/api/products/{productId}/ratings")
public class RatingController {
    private final ProductService productService;
    private final RatingService ratingService;
    private final RatingMapper ratingMapper;
    private final RatingCreateMapper ratingCreateMapper;

    @Autowired
    public RatingController(ProductService productService, RatingService ratingService, RatingMapper ratingMapper, RatingCreateMapper ratingCreateMapper) {
        this.productService = productService;
        this.ratingService = ratingService;
        this.ratingMapper = ratingMapper;
        this.ratingCreateMapper = ratingCreateMapper;
    }

    @GetMapping("")
    public ResponseEntity<Collection<RatingDTO>> getAllRatingsByProduct(@PathVariable Long productId) {
        Collection<Rating> allRatingsByProduct = ratingService.getAllRatingsByProduct(productId);
        List<RatingDTO> allRatingsMapped = allRatingsByProduct.stream().map(ratingMapper::mapTo).toList();
        return ResponseEntity.ok(allRatingsMapped);
    }

    @GetMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> getRating(@PathVariable Long productId, @PathVariable Long ratingId) {
        Optional<Product> productOpt = productService.getProductById(productId);
        if (productOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Optional<Rating> requestedRatingOpt = ratingService.loadRating(productId, ratingId);
        if (requestedRatingOpt.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        Rating requestedRating = requestedRatingOpt.get();
        return ResponseEntity.ok(ratingMapper.mapTo(requestedRating));
    }

    @PostMapping("")
    public ResponseEntity<RatingDTO> createRating(@PathVariable Long productId, @Valid @RequestBody RatingCreateDTO ratingDTO) {
        if (productService.getProductById(productId).isPresent()) {
            Rating rating = ratingService.saveRating(ratingCreateMapper.mapFrom(ratingDTO));
            return ResponseEntity.status(HttpStatus.CREATED).body(ratingMapper.mapTo(rating));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PatchMapping("/{ratingId}")
    public ResponseEntity<RatingDTO> updateRating(@PathVariable Long productId, @PathVariable Long ratingId, @Valid @RequestBody RatingDTO ratingDTO) {
        Optional<Rating> existingRating = ratingService.loadRating(productId, ratingId);
        if (existingRating.isPresent()) {
            Rating rating = ratingMapper.mapFrom(ratingDTO);
            Rating savedRating = ratingService.saveRating(rating);
            return ResponseEntity.ok(ratingMapper.mapTo(savedRating));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{ratingId}")
    public ResponseEntity<Void> deleteRating(@PathVariable Long productId, @PathVariable Long ratingId) {
        Optional<Rating> existingRating = ratingService.loadRating(productId, ratingId);
        if (existingRating.isPresent()) {
            ratingService.deleteRating(existingRating.get());
            return ResponseEntity.noContent().build();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Rating not found");
        }
    }
}
