import {CartDTO} from "../DTO/cart.types";

function getCart(): CartDTO {
    const cartString = localStorage.getItem('cart');

    if (cartString === null) {
        return { items: [] };
    }

    return JSON.parse(cartString) as CartDTO;
}

function saveCart(cart: CartDTO) {
    localStorage.setItem('cart', JSON.stringify(cart));
}

function addToCart(product: ProductDTO, quantity: number) {
    const cart = getCart();
    const existingItem = cart.items.find(item => item.productId === product.id);

    if (existingItem) {
        existingItem.amount += quantity;
    } else {
        cart.items.push({
            productId: product.id,
            pricePerUnit: product.price,
            amount: quantity
        });
    }

    saveCart(cart);
}

function removeFromCart(product: ProductDTO, quantity?: number) {
    const cart = getCart();

    let item = cart.items.find(item => item.productId === product.id);
    if (!item) return;

    if (quantity != null) {
        item.amount -= quantity;
    } else {
        cart.items.filter(item => item.productId !== product.id);
    }
    
    saveCart(cart);
}