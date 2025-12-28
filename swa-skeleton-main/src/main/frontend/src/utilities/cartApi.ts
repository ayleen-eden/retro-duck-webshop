import globalAxios from "axios";
import { CartDTO } from "../DTO/cart.types";

export const validateCart = async (cart: CartDTO): Promise<CartDTO> => {
    try {
        const response = await globalAxios.post("/api/cart/validate", cart);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error validating cart: ${err?.message ?? String(err)}`);
    }
};
