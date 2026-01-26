import {CartDTO, CartItemDTO} from "../DTO/cart.types";
import {ProductDTO} from "../DTO/product.types";

export function getCart(): CartDTO {
    const cartString = localStorage.getItem('cart');

    if (cartString === null) {
        return {items: []};
    }

    return JSON.parse(cartString) as CartDTO;
}

function saveCart(cart: CartDTO) {
    localStorage.setItem('cart', JSON.stringify(cart));
}

export function addToCart(
    cart: CartDTO,
    product: ProductDTO | CartItemDTO,
    quantity: number
): CartDTO {

    const productId = 'id' in product ? product.id : product.productId;
    const pricePerUnit = 'price' in product ? product.price : product.pricePerUnit;
    const productName = 'name' in product ? product.name : product.productName;
    const productImage = 'imageUrl' in product ? product.imageUrl : product.productImage;
    const productDiscount = 'discount' in product ? product.discount : product.productDiscount;
    const productStock = 'stock' in product ? product.stock : product.productStock;

    const existingItem = cart.items.find(i => i.productId === productId);

    const items = existingItem
        ? cart.items.map(item => {
            if (item.productId !== productId) return item;

            const newAmount = item.amount + quantity;
            const cappedAmount = Math.min(newAmount, productStock);

            return {
                ...item,
                amount: cappedAmount,
                productDiscount,
                productStock
            };
        })
        : [
            ...cart.items,
            {
                productId,
                productName,
                productImage,
                pricePerUnit,
                productDiscount,
                productStock,
                amount: Math.min(quantity, productStock)
            }
        ];

    const updatedCart = {
        ...cart,
        items: items.filter(item => item.amount > 0)
    };

    saveCart(updatedCart);
    return updatedCart;
}



export function removeFromCart(cart: CartDTO, product: CartItemDTO, quantity: number): CartDTO {

    const updatedCart: CartDTO = {
        ...cart,
        items: cart.items
            .map(item =>
                item.productId === product.productId
                    ? {...item, amount: item.amount - quantity}
                    : item
            )
            .filter(item => item.amount > 0)
    };

    saveCart(updatedCart);
    return updatedCart;
}

