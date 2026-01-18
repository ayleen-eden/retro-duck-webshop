import {NotificationDTO} from "../DTO/notification.types";
import globalAxios from "axios";

export const getAllNotificationsForUser = async (userId: number): Promise<NotificationDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/notifications/${userId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching notifications for user: ${err?.message ?? String(err)}`);
    }
}