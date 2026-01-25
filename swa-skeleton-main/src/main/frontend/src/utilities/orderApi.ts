import globalAxios from "axios";
import {OrderDTO} from "../DTO/order.types";
import {CheckoutRequestDTO} from "../DTO/checkout.types";

export const OrderApi = {
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

    fetchOrderHistory: async (): Promise<OrderDTO[]> => {
        const response = await globalAxios.get("/api/orders/");
        return response.data;
    },

    deleteOrder: async (orderId: number): Promise<void> => {
        await globalAxios.delete(`/api/orders/${orderId}`);
    }
};