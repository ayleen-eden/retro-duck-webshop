/**
 * Represents the various lifecycle stages an order can go through.
 * <p>
 * These states correspond to the {@code OrderStatus} enum in the Spring Boot backend.
 */
export enum OrderStatus {
    NEW = 'NEW',
    IN_PROGRESS = 'IN_PROGRESS',
    DONE = 'DONE',
}

/**
 * Data Transfer Object representing a single product line item within an order.
 * <p>
 * This interface captures the state of a product (price, discount) at the
 * specific time the order was placed.
 */
export interface OrderItemDTO {
    productId: number;
    productName: string;
    quantity: number;
    priceAtPurchase: number;
    discountAtPurchase: number;
}

/**
 * Data Transfer Object representing a comprehensive summary of an order.
 * <p>
 * Used for displaying order history and order details in the frontend.
 * Matches the structure provided by the backend's {@code OrderDTO}.
 */
export interface OrderDTO {
    id: number;
    orderDate: string;
    status: OrderStatus;
    totalPrice: number;
    items: OrderItemDTO[];
}