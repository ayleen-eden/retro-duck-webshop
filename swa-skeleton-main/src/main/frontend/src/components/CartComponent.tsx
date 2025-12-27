import React, { useState } from "react";
import { CartDTO, CartItemDTO } from "../DTO/cart.types";
import { getCart, removeFromCart, addToCart } from "../utilities/cartUtilities";
import { Button } from "primereact/button";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Card } from 'primereact/card';
import { Divider } from "primereact/divider";
import { ConfirmPopup, confirmPopup } from "primereact/confirmpopup";
import { ScrollTop } from "primereact/scrolltop";
import { dummyCart } from "./DebugDummyCart";
import { Message } from "primereact/message";

const CartComponent: React.FC = () => {
    const USE_DUMMY = false;

    const [cart, setCart] = useState<CartDTO>(
        USE_DUMMY ? dummyCart : getCart()
    );

    const totalPrice = cart.items.reduce((sum, item) => sum + item.amount * item.pricePerUnit, 0).toFixed(2);

    const handleRemove = (item: CartItemDTO) => {
        setCart(prev => removeFromCart(prev, item, item.amount));
    };

    const handleIncrease = (item: CartItemDTO) => {
        setCart(prev => addToCart(prev, item, 1));
    };

    const handleDecrease = (event: React.MouseEvent, item: CartItemDTO) => {
        if (item.amount == 1) {
            confirmDelete(event, item)
        } else {
            setCart(prev => removeFromCart(prev, item, 1));
        }
    };

    const confirmDelete = (event: React.MouseEvent, item: CartItemDTO) => {
        confirmPopup({
            target: event.currentTarget,
            message:`Are you sure you want to remove ${item.productName}?`,
            icon:"pi pi-exclamation-triangle",
            acceptIcon:"pi pi-check",
            rejectIcon:"pi pi-times",
            accept: () => handleRemove(item),
            reject: () => {}
        });
    };

    const actionBodyTemplate = (rowData: CartItemDTO) => {
        return (
            <div>
                <Button icon="pi pi-plus" size="small" severity="secondary" onClick={() => handleIncrease(rowData)} text/>
                <Button icon="pi pi-minus" size="small" severity="secondary" onClick={(event) => handleDecrease(event, rowData)} text/>
                <Button
                    icon="pi pi-trash"
                    severity="danger"
                    outlined
                    onClick={(event) => confirmDelete(event, rowData)}
                />
            </div>
        );
    };

    const imageBodyTemplate = (rowData: CartItemDTO) => {
        return ( <img src={rowData.productImage} alt={rowData.productName} style={{ width: 50, height: 50, objectFit: 'cover' }}/> );
    };
    const amountBodyTemplate = (rowData: CartItemDTO) => rowData.amount;
    const priceBodyTemplate = (rowData: CartItemDTO) => `${rowData.pricePerUnit.toFixed(2)} €`;
    const totalBodyTemplate = (rowData: CartItemDTO) => `${(rowData.amount * rowData.pricePerUnit).toFixed(2)} €`;

    return (<Card title="Shopping cart" className="m-4">
        <ConfirmPopup/>
        <ScrollTop/>
        <div>
            {cart.items.length === 0 ? (
                <div style={{
                    display:"flex",
                    justifyContent:"center",
                    alignItems:"center"
                }}>
                    <div style={{
                        textAlign:"center",
                        marginTop:"50px",
                        padding:"40px",
                        backgroundColor:"ghostwhite",
                        maxWidth:"400px",
                        boxShadow:"0 4px 8px rgba(0,0,0,0.1)"}}
                    >
                        <i className="pi pi-shopping-cart" style={{ fontSize: '2.5rem' }}/>
                        <h2>Your cart is empty!</h2>
                        <p>Looks like you haven’t added anything yet.</p>
                        <Divider type="dashed"/>
                        <Button
                            label="Go shopping"
                            icon="pi pi-cart-plus"
                            severity="success"
                            onClick={() => window.location.href = "/"}
                            text raised
                            rounded
                        />
                    </div>
                </div>
            ) : (
            <>
                <div style={{
                    display:"flex",
                    justifyContent:"center",
                    alignItems:"center",
                    marginBottom:"30px"
                }}>
                    <i className="pi pi-shopping-cart" style={{ fontSize: '4rem', color: 'var(--primary-color)' }} />
                </div>
                <DataTable value={cart.items} dataKey="productId" stripedRows>
                        <Column
                            field="productName"
                            header="Product"
                        />
                        <Column
                            header="Image"
                            body={imageBodyTemplate}
                        />
                        <Column
                            header="Amount"
                            body={amountBodyTemplate}
                            sortable
                        />
                        <Column
                            header="Price per unit"
                            body={priceBodyTemplate}
                            sortable
                        />
                        <Column
                            header="Total price"
                            body={totalBodyTemplate}
                            sortable
                        />
                        <Column
                            body={actionBodyTemplate}
                        />
                </DataTable>
                <Divider type="dashed"/>
                <div style={{
                    display:"flex",
                    justifyContent:"center",
                    alignItems:"center"
                }}>
                    <Message
                        severity="info"
                        content={
                            <div>
                                <i className="pi pi-wallet text-xl"></i>
                                <b> Total: {totalPrice} €</b>
                            </div>
                        }
                    />
                </div>
                <Divider type="dashed" align="center">
                    <Button
                        icon="pi pi-money-bill"
                        label="Proceed to checkout"
                        size="large"
                        severity="success"
                        raised
                    />
                </Divider>
            </>
            )}
        </div>
        </Card>
    );
};

export default CartComponent;
