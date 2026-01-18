import globalAxios from "axios";
import {SubscriptionDTO} from "../DTO/subscription.types";

export const subscribe = async (userId: number, productId: number): Promise<SubscriptionDTO> => {
    try {
        const response = await globalAxios.post(`/api/subscriptions`, {}, {params: {userId: userId, productId: productId}});
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching notifications for user: ${err?.message ?? String(err)}`);
    }
}

export const unsubscribe = async (userId: number, productId: number): Promise<void> => {
    try {
        await globalAxios.delete(`/api/subscriptions`, {params: {userId: userId, productId: productId}});
    } catch (err: any) {
        throw new Error(`Error unsubscribing: ${err?.message ?? String(err)}`);
    }
}

export const getAllSubscriptionsForUser = async (userId: number): Promise<SubscriptionDTO[]> => {
    try {
        const response = await globalAxios.get(`/api/subscriptions/${userId}`);
        return response.data;
    } catch (err: any) {
        throw new Error(`Error fetching subscriptions for user: ${err?.message ?? String(err)}`);
    }
}