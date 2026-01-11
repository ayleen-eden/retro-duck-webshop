export enum OrderStatus {
    IN_PROGRESS = 'IN_PROGRESS',
    DONE = 'DONE',
    CANCELLED = 'CANCELLED'
}

export interface OrderItemDTO {
    productId: number;
    productName: string;
    quantity: number;
    priceAtPurchase: number;
    discountAtPurchase: number;
}

export interface OrderDTO {
    id: number;
    orderDate: string;
    status: OrderStatus;
    totalPrice: number;
    items: OrderItemDTO[];
}