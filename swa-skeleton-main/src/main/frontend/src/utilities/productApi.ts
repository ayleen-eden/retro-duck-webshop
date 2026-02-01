import {ProductCreateDTO, ProductDTO} from "../DTO/product.types";
import globalAxios from "axios";

/**
 * Service object providing API access for product-related operations.
 * <p>
 * This service handles communication with the Spring Boot backend's product endpoints.
 * It uses {@code globalAxios} for standardized HTTP requests.
 */

/**
 * Fetches all products currently available in the shop.
 * * @returns A promise resolving to an array of {@link ProductDTO}s.
 * @throws Error if the network request fails or the server returns an error.
 */
export const getAllProducts = async (): Promise<ProductDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/products/`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching products: ${err?.message ?? String(err)}`);
    }
}

/**
 * Retrieves a single product by its unique identifier.
 * * @param productId - The unique ID of the product to fetch.
 * @returns A promise resolving to the requested {@link ProductDTO}.
 * @throws Error if the product is not found (404) or a server error occurs.
 */
export const getProductById = async (productId: number): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.get(`/api/products/${productId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching product: ${err?.message ?? String(err)}`);
    }
}

/**
 * Sends a request to create a new product in the database.
 * <p>
 * This operation is typically restricted to administrative roles (MANAGER, ADMIN).
 * * @param product - The product data to be created.
 * @returns A promise resolving to the newly created {@link ProductDTO}.
 * @throws Error if validation fails or unauthorized access is attempted.
 */
export const createProduct = async (product: ProductCreateDTO): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.post(`/api/products/`, product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error creating product: ${err?.message ?? String(err)}`);
    }
}

/**
 * Updates an existing product using a partial update (PATCH).
 * <p>
 * Restricted to administrative roles.
 * @param productId - The ID of the product to update.
 * @param product - The updated product data.
 * @returns A promise resolving to the updated {@link ProductDTO}.
 * @throws Error if the product does not exist or update fails.
 */
export const updateProduct = async (productId: number, product: ProductDTO): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.patch(`/api/products/${productId}`, product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error updating product: ${err?.message ?? String(err)}`);
    }
}

/**
 * Deletes a product from the system.
 * <p>
 * Restricted to administrative roles.
 * @param productId - The unique ID of the product to be removed.
 * @returns A promise that resolves when the deletion is successful.
 * @throws Error if the product cannot be deleted or does not exist.
 */
export const deleteProduct = async (productId: number): Promise<void> => {
    try {
        const response = await globalAxios.delete(`/api/products/${productId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error deleting product: ${err?.message ?? String(err)}`);
    }
}

/**
 * Fetches all available product category names from the backend.
 * * @returns A promise resolving to a string array of category names.
 */
const fetchCategories = async (): Promise<string[]> => {
    try {
        const response = await globalAxios.get("/api/products/categories");
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching categories: ${err?.message ?? String(err)}`);
    }
};



export const ProductApi = {
    createProduct,
    updateProduct,
    deleteProduct,
    getAllProducts,
    getProductById,
    fetchCategories
};