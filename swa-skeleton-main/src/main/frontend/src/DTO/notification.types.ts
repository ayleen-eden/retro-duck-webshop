export enum NotificationType {
    RESTOCK = 'RESTOCK',
    SALE ='SALE',
    OUT_OF_STOCK = 'OUT_OF_STOCK'
}

export enum NotificationChannelType {
    SMS = 'SMS',
    WHATSAPP ='WHATSAPP',
    EMAIL = 'EMAIL'
}

export interface NotificationDTO {
    id: number,
    productId: number,
    userId: number,
    description: string,
    title: string,
    timestamp: Date,
    type: NotificationType
}