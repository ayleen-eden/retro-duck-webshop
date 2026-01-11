package at.qe.skeleton.repositories;

import at.qe.skeleton.model.Order;
import at.qe.skeleton.model.Userx;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends AbstractRepository<Order, Long> {

    List<Order> findByUser(Userx user);

}