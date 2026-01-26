export interface CartItemDTO {
    productId: number,
    productName: string,
    productImage:string,
    pricePerUnit: number,
    amount: number,
    productDiscount: number,
    totalPrice?: number; //optional field for Datatable in CartComponent.tsx
    productStock: number
}

export interface CartDTO {
    items: CartItemDTO[]
}