import React, { useState } from "react";
import { CartDTO, CartItemDTO } from "../DTO/cart.types";
import { getCart, removeFromCart, addToCart } from "../utilities/cartUtilities";
import { Button } from "primereact/button";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Card } from 'primereact/card';
import { ButtonGroup } from "primereact/buttongroup";
import { FilterService } from "primereact/api";
import { Divider } from "primereact/divider";
import { ConfirmPopup, confirmPopup } from "primereact/confirmpopup";
import {ScrollTop } from "primereact/scrolltop";

FilterService.register('custom_range', (value, filters) => {
        const [from, to] = filters ?? [null, null];
        if (from === null && to === null) return true;
        if (from !== null && to === null) return from <= value;
        if (from === null && to !== null) return value <= to;
        return from <= value && value <= to;
});

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

        const confirmDelete = (event: React.MouseEvent, item: CartItemDTO) => {
            confirmPopup({
                target: event.currentTarget,
                message:"Are you sure you want to remove ${item.productName}?",
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
                    <ButtonGroup>
                        <Button icon="pi pi-plus" size="small" severity="secondary" onClick={() => handleIncrease(rowData)} text/>
                        <Button icon="pi pi-minus" size="small" severity="danger" onClick={() => handleDecrease(rowData)} text/>
                    </ButtonGroup>
                    <Button
                        icon="pi pi-trash"
                        severity="danger"
                        rounded
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

        const priceRowFilterTemplate = (options: any) => {
                const { filterCallback } = options;
                return (
                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                                <input
                                        type="number"
                                        placeholder="From"
                                        value={options.filters?.[0] ?? ''}
                                        onChange={(e) => filterCallback([e.target.value ? parseFloat(e.target.value) : null, options.filters?.[1] ?? null])}
                                        style={{ width: '4rem' }}
                                />
                                <input
                                        type="number"
                                        placeholder="To"
                                        value={options.filters?.[1] ?? ''}
                                        onChange={(e) => filterCallback([options.filters?.[0] ?? null, e.target.value ? parseFloat(e.target.value) : null])}
                                        style={{ width: '4rem' }}
                                />
                        </div>
                );
        };
        const amountRowFilterTemplate = (options: any) => {
                const { filterCallback } = options;
                return (
                        <div style={{ display: 'flex', gap: '0.5rem' }}>
                                <input
                                        type="number"
                                        placeholder="From"
                                        value={options.filters?.[0] ?? ''}
                                        onChange={(e) => filterCallback([e.target.value ? parseInt(e.target.value) : null, options.filters?.[1] ?? null])}
                                        style={{ width: '3rem' }}
                                />
                                <input
                                        type="number"
                                        placeholder="To"
                                        value={options.filters?.[1] ?? ''}
                                        onChange={(e) => filterCallback([options.filters?.[0] ?? null, e.target.value ? parseInt(e.target.value) : null])}
                                        style={{ width: '3rem' }}
                                />
                        </div>
                );
        };

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
                    <DataTable value={cart.items} stripedRows>
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
                                    filter
                                    filterField="amount"
                                    filterMatchMode="custom_range"
                                    filterElement={amountRowFilterTemplate}
                            />
                            <Column
                                    header="Price per unit"
                                    body={priceBodyTemplate}
                                    sortable
                                    filter
                                    filterField="pricePerUnit"
                                    filterMatchMode="custom_range"
                                    filterElement={priceRowFilterTemplate}
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
                    <p>
                            <i className="pi pi-wallet"/>
                            Total: {cart.items.reduce((sum, item) => sum + item.amount * item.pricePerUnit, 0).toFixed(2)} €
                    </p>
                    <Button
                        icon="pi pi-money-bill"
                        label="Checkout"
                        size="large"
                        severity="success"
                    /> // ! TODO: implement checkout
                </>
                )}
            </div>
            </Card>
        );
};

export default CartComponent;
