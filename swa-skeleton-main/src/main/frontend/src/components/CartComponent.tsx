import React, { useState } from "react";
import { CartDTO, CartItemDTO } from "../DTO/cart.types";
import { getCart, removeFromCart, addToCart } from "../utilities/cartUtilities";
import { Button } from "primereact/button";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";

const CartComponent: React.FC = () => {
        const [cart, setCart] = useState<CartDTO>(getCart());

        const handleRemove = (item: CartItemDTO) => {
                const updatedCart = removeFromCart(item);
                setCart(updatedCart);
        };

        const handleIncrease = (item: CartItemDTO) => {
                const updatedCart = addToCart(item, 1);
                setCart(updatedCart);
        };

        const handleDecrease = (item: CartItemDTO) => {
                const updatedCart = removeFromCart(item, 1);
                setCart(updatedCart);
        };

        const actionBodyTemplate = (rowData: CartItemDTO) => {
                return (
                        <div>
                                <Button icon="pi pi-plus" size="small" severity="secondary" onClick={() => handleIncrease(rowData)}/>
                                <Button icon="pi pi-minus" size="small" severity="danger" onClick={() => handleDecrease(rowData)}/>
                                <Button icon="pi pi-trash" size="small" severity="secondary" onClick={() => handleRemove(rowData)}/>
                        </div>
                );
        };

        const imageBodyTemplate = (rowData: CartItemDTO) => {
                return ( <img src={rowData.productImage} alt={rowData.productName} style={{ width: 50, height: 50, objectFit: 'cover' }}/> );
        };
        const amountBodyTemplate = (rowData: CartItemDTO) => rowData.amount;
        const priceBodyTemplate = (rowData: CartItemDTO) => `${rowData.pricePerUnit.toFixed(2)} €`;
        const totalBodyTemplate = (rowData: CartItemDTO) => `${(rowData.amount * rowData.pricePerUnit).toFixed(2)} €`;

        return (
                <div>
                        <h1>Shopping Cart</h1>
                        {cart.items.length === 0 ? (
                        <p>Your cart is empty.</p>
                        ) : (
                        <>
                                <DataTable value={cart.items} stripedRows>
                                        <Column field="productName" header="Product"/>
                                        <Column header="Image" body={imageBodyTemplate}/>
                                        <Column header="Amount" body={amountBodyTemplate}/>
                                        <Column header="Price per unit" body={priceBodyTemplate}/>
                                        <Column header="Total price" body={totalBodyTemplate}/>
                                        <Column body={actionBodyTemplate}/>
                                </DataTable>
                                <p>Total: {cart.items.reduce((sum, item) => sum + item.amount * item.pricePerUnit, 0).toFixed(2)} €</p>
                                <Button icon="pi pi-money-bill" label="Checkout" size="large" severity="success"/> // ! TODO: implement checkout
                        </>
                        )}
                </div>
        );
};

export default CartComponent;
