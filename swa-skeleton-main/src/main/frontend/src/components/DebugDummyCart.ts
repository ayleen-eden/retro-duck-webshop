import { CartDTO } from "../DTO/cart.types";

export const dummyCart: CartDTO = {
    items: [
        {
            productId: 1,
            productName: "Coffee Mug",
            productImage: "images/mug.png",
            pricePerUnit: 5.99,
            productDiscount: 0.3,
            amount: 2
        },
        {
            productId: 2,
            productName: "Fancy Tea",
            productImage: "images/tea.png",
            pricePerUnit: 3.49,
            productDiscount: 0,
            amount: 3
        },
        {
            productId: 3,
            productName: "Debug Duck",
            productImage: "images/duck.png",
            pricePerUnit: 12.99,
            productDiscount: 0.5,
            amount: 1
        }
    ]
};
