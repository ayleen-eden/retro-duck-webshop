export interface CartItemDTO {
    productId: number,
    productName: string,
    productImage:string,
    pricePerUnit: number,
    amount: number
}

export interface CartDTO {
    items: CartItemDTO[]
}