import {ProductDTO} from "../DTO/product.types";

export const dummyProduct1: ProductDTO = {
    id: 1,
    price: 29.99,
    name: 'Kazuma Kiryu Duck',
    imageUrl: '/images/kiryu_duck.png',
    description: 'The Dragon of Dojima as a duck.',
    stock: 10,
    discount: 0.1,
    categories: ['YAKUZA', 'DUCK']
};

export const dummyProduct2: ProductDTO = {
    id: 2,
    price: 29.99,
    name: 'Goro Majima Duck',
    imageUrl: '/images/majima_duck.png',
    description: 'Mad Dog of Shimano.',
    stock: 5,
    discount: 0.0,
    categories: ['YAKUZA', 'DUCK']
};