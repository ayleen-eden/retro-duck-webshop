import {CartDTO, CartItemDTO} from "../DTO/cart.types";

export function getCart(): CartDTO {
    const cartString = localStorage.getItem('cart');

    if (cartString === null) {
        return { items: [] };
    }

    return JSON.parse(cartString) as CartDTO;
}

function saveCart(cart: CartDTO) {
    localStorage.setItem('cart', JSON.stringify(cart));
}

export function addToCart(product: ProductDTO | CartItemDTO, quantity: number): CartDTO {
    const cart = getCart();

    const productId = 'id' in product ? product.id : product.productId;
    const pricePerUnit = 'price' in product ? product.price : product.pricePerUnit;
    const productName = 'name' in product ? product.name : product.productName;
    const productImage = 'image' in product ? product.image : product.productImage;

    const existingItem = cart.items.find(item => item.productId === productId);

    let updatedCart: CartDTO;

    if (existingItem) {
        updatedCart = {
            ...cart,
            items: cart.items.map(item =>
                item.productId === productId
                    ? { ...item, amount: Math.max(item.amount + quantity, 0) }
                    : item
            )
        };
    } else {
        updatedCart = {
            ...cart,
            items: [...cart.items, { productId, productName, productImage, pricePerUnit, amount: quantity }]
        };
    }

    saveCart(updatedCart);
    return updatedCart;
}

export function removeFromCart(product: ProductDTO | CartItemDTO, quantity?: number): CartDTO {
    const cart = getCart();
    const productId = 'id' in product ? product.id : product.productId;

    let updatedCart: CartDTO;

    if (quantity != null) {
        updatedCart = {
            ...cart,
            items: cart.items
                .map(item =>
                    item.productId === productId
                        ? { ...item, amount: Math.max(item.amount - quantity, 0) }
                        : item
                )
                .filter(item => item.amount > 0)
        };
    } else {
        updatedCart = {
            ...cart,
            items: cart.items.filter(item => item.productId !== productId)
        };
    }

    saveCart(updatedCart);
    return updatedCart;
}
