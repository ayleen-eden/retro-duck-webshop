import {CartDTO} from "./cart.types";

export interface CheckoutRequestDTO {
    cart: CartDTO;
    shippingName: string;
    shippingStreet: string;
    shippingCity: string;
    // Postal Codes (worldwide) do not always contain of numbers only (see: https://en.wikipedia.org/wiki/List_of_postal_codes)
    shippingPostalCode: string;
    shippingCountry: string;
    paymentMethod: string;
}