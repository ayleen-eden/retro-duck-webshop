import {SubscriptionDTO} from "../DTO/subscription.types";

const dummySubscription1: SubscriptionDTO = {
    id: 1,
    productId: 1,
    userId: 1
}

const dummySubscription2: SubscriptionDTO = {
    id: 1,
    productId: 2,
    userId: 1
}

export const dummySubscriptions: SubscriptionDTO[] = [dummySubscription1, dummySubscription2];