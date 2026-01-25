import {NotificationDTO, NotificationType} from "../DTO/notification.types";

const dummyNotification1: NotificationDTO = {
    id: 1,
    productId: 1,
    userId: 1,
    description: "You hoped for a sale, didn't you?",
    title: "HOT NEW SALE!",
    timestamp: new Date("2026-01-01"),
    type: NotificationType.SALE
}

const dummyNotification2: NotificationDTO = {
    id: 2,
    productId: 2,
    userId: 1,
    description: "KIRYU-CHAAAAAAAN!",
    title: "MAD DOG OUT OF STOCK!",
    timestamp: new Date("2026-02-01"),
    type: NotificationType.OUT_OF_STOCK
}

export const dummyNotifications: NotificationDTO[] = [dummyNotification1, dummyNotification2];