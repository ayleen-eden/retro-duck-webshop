export interface CartItemDTO {
    productId: number;
    pricePerUnit: number,
    amount: number
}

export interface CartDTO {
    items?: CartItemDTO[]
}