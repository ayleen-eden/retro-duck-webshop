package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Subscription;
import java.util.Collection;

/**
 * Repository for managing {@link Subscription} entities.
 */
public interface SubscriptionRepository extends AbstractRepository<Subscription, Long> {

    Collection<Subscription> findByUserId(Long id);

    Collection<Subscription> findByProductId(Long id);

    Subscription findByUserIdAndProductId(Long userId, Long productId);
}
