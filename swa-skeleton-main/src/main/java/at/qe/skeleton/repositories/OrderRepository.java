package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Order;
import at.qe.skeleton.model.Userx;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository interface for {@link Order} entities.
 * <p>
 * This interface handles the persistence operations for orders by extending
 * the {@link AbstractRepository}. It provides custom query methods to retrieve
 * order data from the database, such as filtering by a specific user.
 */
@Repository
public interface OrderRepository extends AbstractRepository<Order, Long> {

    /**
     * Retrieves all orders placed by a specific user.
     *
     * @param user the {@link Userx} entity whose orders should be retrieved.
     * @return a {@link List} of {@link Order} entities associated with the given user.
     */
    List<Order> findByUser(Userx user);

}