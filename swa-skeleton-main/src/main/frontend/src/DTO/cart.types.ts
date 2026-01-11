export interface CartItemDTO {
    productId: number,
    productName: string,
    productImage:string,
    pricePerUnit: number,
    amount: number
    totalPrice?: number; //optional field for Datatable in CartComponent.tsx
}

export interface CartDTO {
    items: CartItemDTO[]
}