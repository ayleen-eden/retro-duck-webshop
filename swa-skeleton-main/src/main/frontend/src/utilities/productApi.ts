import globalAxios from "axios";
import {ProductDTO} from "../DTO/product.types";

const createProduct = async (product: Partial<ProductDTO>): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.post("/api/products/", product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error creating product: ${err?.message ?? String(err)}`);
    }
};

const updateProduct = async (id: number, product: Partial<ProductDTO>): Promise<ProductDTO> => {
    try {
        const response = await globalAxios.patch(`/api/products/${id}`, product);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error updating product: ${err?.message ?? String(err)}`);
    }
};

const deleteProduct = async (id: number): Promise<void> => {
    try {
        await globalAxios.delete(`/api/products/${id}`);
    } catch (err: any) {
        throw new Error(`Error deleting product: ${err?.message ?? String(err)}`);
    }
};

const fetchAllProducts = async (): Promise<ProductDTO[]> => {
    try {
        const response = await globalAxios.get("/api/products/");
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching products: ${err?.message ?? String(err)}`);
    }
};

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
    fetchAllProducts,
    fetchCategories
};