import {ProductCreateDTO, ProductDTO} from "../DTO/product.types";
import globalAxios from "axios";

export const getAllProducts = async (): Promise<ProductDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/products/`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching products: ${err?.message ?? String(err)}`);
    }
}

export const getProductById = async (productId: number): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.get(`/api/products/${productId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching product: ${err?.message ?? String(err)}`);
    }
}

export const createProduct = async (product: ProductCreateDTO): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.post(`/api/products/`, product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error creating product: ${err?.message ?? String(err)}`);
    }
}

export const updateProduct = async (productId: number, product: ProductDTO): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.patch(`/api/products/${productId}`, product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error updating product: ${err?.message ?? String(err)}`);
    }
}


export const deleteProduct = async (productId: number): Promise<void> => {
    try {
        const response = await globalAxios.delete(`/api/products/${productId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error deleting product: ${err?.message ?? String(err)}`);
    }
}