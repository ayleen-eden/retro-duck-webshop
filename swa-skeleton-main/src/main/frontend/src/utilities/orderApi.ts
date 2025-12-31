import globalAxios from "axios";
import { CartDTO } from "../DTO/cart.types";
import { OrderDTO } from "../DTO/order.types";

export const createOrder = async (cart: CartDTO): Promise<OrderDTO> => {
    try {
        const response = await globalAxios.post("/api/orders/", cart);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error creating order: ${err?.message ?? String(err)}`);
    }
};

