import globalAxios from "axios";
import {OrderDTO} from "../DTO/order.types";
import {CheckoutRequestDTO} from "../DTO/checkout.types";

/**
 * Service object providing API access for order-related operations.
 * <p>
 * This service handles communication with the Spring Boot backend's order endpoints.
 * It uses {@code globalAxios} for standardized HTTP requests.
 */
export const OrderApi = {
    /**
     * Submits a new order to the backend.
     * <p>
     * Before sending, this method maps the frontend cart items to the specific
     * structure required by the backend. It handles the snapshots of prices
     * and discounts at the time of purchase.
     * * @param checkoutData - The object containing cart items and shipping/payment details.
     * @returns A promise that resolves to the finalized {@link OrderDTO} from the server.
     * *@throws Will encounter an HTTP 409 error if the backend throws an {@code InsufficientStockException}.
     */
    createOrder: async (checkoutData: CheckoutRequestDTO): Promise<OrderDTO> => {
        const cleanItems = checkoutData.cart.items.map(item => ({
            productId: item.productId,
            productName: item.productName,
            productImage: item.productImage,
            pricePerUnit: item.pricePerUnit,
            productDiscount: item.productDiscount,
            amount: item.amount
        }));

        const payload = {
            ...checkoutData, // ... -> Spread-Operator
            cart: {items: cleanItems}
        };

        const response = await globalAxios.post("/api/orders/", payload);
        return response.data;
    },

    /**
     * Fetches the complete order history for the currently authenticated user.
     * * @returns A promise resolving to an array of {@link OrderDTO}s.
     */
    fetchOrderHistory: async (): Promise<OrderDTO[]> => {
        const response = await globalAxios.get("/api/orders/");
        return response.data;
    },

    /**
     * Deletes a specific order by its unique identifier.
     * * @param orderId - The unique ID of the order to be deleted.
     * *@throws Will fail with HTTP 403/404 if the backend throws
     * {@code UnauthorizedOrderAccessException} or {@code OrderNotFoundException}.
     */
    deleteOrder: async (orderId: number): Promise<void> => {
        await globalAxios.delete(`/api/orders/${orderId}`);
    }
};