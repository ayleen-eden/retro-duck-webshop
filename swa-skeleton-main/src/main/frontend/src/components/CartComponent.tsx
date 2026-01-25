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
import { OrderApi } from "../utilities/orderApi";
import { FilterService } from "primereact/api";
import { InputNumber } from "primereact/inputnumber";
import { useNavigate } from 'react-router-dom';
import { ROUTES } from "../utilities/routes.paths";
import styles from "./PixelButton.module.css"

FilterService.register('custom_range', (value, filters) => {
    const [from, to] = filters ?? [null, null];
    if (from === null && to === null) return true;
    if (from !== null && to === null) return from <= value;
    if (from === null && to !== null) return value <= to;
    return from <= value && value <= to;
});

const CartComponent: React.FC = () => {
    const USE_DUMMY = false;

    const navigate = useNavigate();

    const [cart, setCart] = useState<CartDTO>(
        USE_DUMMY ? dummyCart : getCart()
    );

    const cartWithTotals: CartItemDTO[] = cart.items.map(item => ({
        ...item,
        totalPrice: item.amount * item.pricePerUnit
    }));

    const totalPrice = cart.items.reduce((sum, item) => sum + item.amount * item.pricePerUnit, 0).toFixed(2);

    const handleCheckout = async () => {
        try {
            await OrderApi.createOrder(cart);
            localStorage.removeItem('cart');
            setCart({ items: [] });
            alert("Order created! Thank you for shopping with us!");
            navigate(ROUTES.ORDERS);
        } catch (error: any) {
            if (error.response && error.response.status === 409) {
                alert("Failed to create order: " + error.response.data); // Hier wäre ein Toast schöner
            } else {
                alert("Failed to create order: Unknown error");
            }
        }
    };

    const handleRemove = (item: CartItemDTO) => {
        setCart(prev => removeFromCart(prev, item, item.amount));
    };
    const handleIncrease = (item: CartItemDTO) => {
        setCart(prev => addToCart(prev, item, 1));
    };
    const handleDecrease = (event: React.MouseEvent<HTMLButtonElement>, item: CartItemDTO) => {
        if (item.amount === 1) {
            confirmDelete(event, item)
        } else {
            setCart(prev => removeFromCart(prev, item, 1));
        }
    };
    const confirmDelete = (event: React.MouseEvent<HTMLButtonElement>, item: CartItemDTO) => {
        confirmPopup({
            className:"pixel-confirmpopup pixel-icon",
            target: event.currentTarget,
            message:`ARE YOU SURE YOU WANT TO REMOVE ${item.productName}?`,
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
                <Button className={`${styles.btn} ${styles.btn_red}`} icon="pi pi-minus" size="small" onClick={(event) => handleDecrease(event, rowData)} text/>
                <Button className={`${styles.btn} ${styles.btn_green}`} icon="pi pi-plus" size="small" onClick={() => handleIncrease(rowData)} text/>
                <Button
                    className={`${styles.btn} ${styles.btn_grey}`}
                    icon="pi pi-trash"
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

    const numericRangeFilterTemplate = (options: any) => {
        const [from, to] = options.value ?? [null, null];

        return (
            <div className="flex gap-1">
                <InputNumber value={from} onChange={(e) => options.filterApplyCallback([e.value, to])} placeholder="FROM" allowEmpty style={{ width: '6rem' }}/>
                <InputNumber value={to} onChange={(e) => options.filterApplyCallback([from, e.value])} placeholder="TO" allowEmpty style={{ width: '6rem' }}/>
            </div>
        );
    };

    return (<Card title="MY SHOPPING CART" className="product-card">
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
                            <i className="pi pi-shopping-cart pixel-icon pixel-icon-blue" style={{ fontSize: '2.5rem' }}/>
                            <h2 style={{color: 'black'}}> YOUR CART IS EMPTY!</h2>
                            <p>LOOKS LIME YOU HAVEN'T ADDED ANYTHING YET.</p>
                            <Divider className="pixel-divider-dashed"/>
                            <Button
                                className={`${styles.btn} ${styles.btn_yellow}`}
                                label="GO SHOPPING"
                                icon="pi pi-cart-plus"
                                severity="success"
                                onClick={() => window.location.href = "/"}
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
                            <i className="pi pi-shopping-cart pixel-icon pixel-icon-blue"  style={{ fontSize: '4rem' }}/>
                        </div>
                        <DataTable<CartItemDTO[]>
                            value={cartWithTotals}
                            dataKey="productId"
                            stripedRows
                            filterDisplay="row"
                            emptyMessage="NO DUCKS FOUND MATCHING YOUR CRITERIA. 🦆"
                        >
                            <Column
                                field="productName"
                                header="PRODUCT"
                                sortable
                            />
                            <Column
                                header="IMAGE"
                                body={imageBodyTemplate}
                            />
                            <Column
                                field="amount"
                                header="AMOUNT"
                                body={amountBodyTemplate}
                                sortable
                                filter
                                showFilterMenu={false}
                                showClearButton={false}
                                filterMatchMode="custom_range"
                                filterElement={numericRangeFilterTemplate}
                            />
                            <Column
                                field="pricePerUnit"
                                header="PRICE PER UNIT"
                                body={priceBodyTemplate}
                                sortable
                                filter
                                showFilterMenu={false}
                                showClearButton={false}
                                filterMatchMode="custom_range"
                                filterElement={numericRangeFilterTemplate}
                            />
                            <Column
                                field="totalPrice"
                                header="TOTAL PRICE"
                                body={totalBodyTemplate}
                                sortable
                                filter
                                showFilterMenu={false}
                                showClearButton={false}
                                filterMatchMode="custom_range"
                                filterElement={numericRangeFilterTemplate}
                            />
                            <Column
                                body={actionBodyTemplate}
                            />
                        </DataTable>
                        <Divider className="pixel-divider-dashed"/>
                        <div style={{
                            display:"flex",
                            justifyContent:"center",
                            alignItems:"center"
                        }}>
                            <Message
                                className="pixel-message pixel-message-info"
                                severity="info"
                                content={
                                    <div>
                                        <i className="pi pi-wallet text-xl"></i>
                                        <b> TOTAL: {totalPrice} €</b>
                                    </div>
                                }
                            />
                        </div>
                        <Divider className="pixel-divider-dashed" align="center"/>
                        <Button
                            className={`${styles.btn} ${styles.btn_green}`}
                            icon="pi pi-money-bill"
                            label="PROCEED TO CHECKOUT"
                            size="large"
                            severity="success"
                            raised
                            onClick={handleCheckout}
                        />
                    </>
                )}
            </div>
        </Card>
    );
};

export default CartComponent;