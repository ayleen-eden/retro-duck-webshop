import globalAxios from "axios";
import { CartDTO } from "../DTO/cart.types";
import { OrderDTO } from "../DTO/order.types";

export const OrderApi = {
    createOrder: async (cart: CartDTO): Promise<OrderDTO> => {
        const cleanItems = cart.items.map(item => ({
            productId: item.productId,
            productName: item.productName,
            productImage: item.productImage,
            pricePerUnit: item.pricePerUnit,
            productDiscount: item.productDiscount,
            amount: item.amount
        }));

        const cleanCart = {
            items: cleanItems
        };

        const response = await globalAxios.post("/api/orders/", cleanCart);
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