export interface ProductDTO {
    id: number;
    name: string;
    description: string;
    price: number;
    stock: number;
    discount: number;
    imageUrl: string;
    categories: string[];
}