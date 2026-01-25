import React, { useEffect, useState } from 'react';
import {DataTable} from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';
import { OrderApi } from "../utilities/orderApi";
import {OrderDTO, OrderItemDTO, OrderStatus} from "../DTO/order.types";
import {Button} from "primereact/button";
import styles from "../components/PixelButton.module.css"
import {ConfirmPopup, confirmPopup} from "primereact/confirmpopup";

const OrderHistoryComponent: React.FC = () => {
    const [orders, setOrders] = useState<OrderDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [expandedRows, setExpandedRows] = useState<any>(null);

    useEffect(() => {
        loadOrders();
    }, []);

    const loadOrders = async () => {
        setLoading(true);
        try {
            const data = await OrderApi.fetchOrderHistory();
            setOrders(data);
        } catch (error) {
            console.error("Error loading orders", error);
        } finally {
            setLoading(false);
        }
    };

    const confirmDelete = (event: React.MouseEvent<HTMLButtonElement>, orderId: number) => {
        confirmPopup({
            className: "pixel-confirmpopup pixel-icon",
            target: event.currentTarget,
            message: `ARE YOU SURE YOU WANT TO DELETE ORDER #${orderId}?`,
            icon: "pi pi-exclamation-triangle",
            acceptIcon: "pi pi-check",
            rejectIcon: "pi pi-times",
            accept: () => handleDelete(orderId),
            reject: () => {}
        });
    };

    const handleDelete = async (orderId: number) => {
        try {
            await OrderApi.deleteOrder(orderId);
            setOrders(prev => prev.filter(o => o.id !== orderId));
        } catch (error: any) {
            if (error.response?.status === 403) {
                alert("Forbidden: You cannot delete this order.");
            } else if (error.response?.status === 404) {
                alert("Not Found: This order does not exist anymore.");
            } else {
                alert("Error: Could not delete order.");
            }
        }
    };

    const statusBodyTemplate = (rowData: OrderDTO) => {
        const severity = rowData.status === OrderStatus.DONE ? 'success' :
            rowData.status === OrderStatus.CANCELLED ? 'danger' : 'info';
        return <Tag className="pixel-tag pixel-tag-blue" value={rowData.status} severity={severity} />;
    };

    const dateBodyTemplate = (rowData: OrderDTO) => {
        return new Date(rowData.orderDate).toLocaleString();
    };

    const priceBodyTemplate = (rowData: OrderDTO) => {
        return `${rowData.totalPrice.toFixed(2)} €`;
    };

    const rowExpansionTemplate = (data: OrderDTO) => {
        return (
            <div className="p-3" style={{ backgroundColor: 'rgba(0,0,0,0.05)', borderRadius: '8px' }}>
                <h5 style={{ marginTop: 0 }}>Details for Order #{data.id}</h5>
                <ConfirmPopup />
                <DataTable<OrderItemDTO[]> value={data.items} className="pixel-table-nested">
                    <Column field="productName" header="PRODUCT" />
                    <Column field="quantity" header="QUANTITY" />
                    <Column
                        field="priceAtPurchase"
                        header="UNIT PRICE"
                        body={(item: OrderItemDTO) => `${item.priceAtPurchase.toFixed(2)} €`}
                    />
                    <Column
                        header="SUBTOTAL"
                        body={(item: OrderItemDTO) => `${(item.quantity * item.priceAtPurchase).toFixed(2)} €`}
                    />
                </DataTable>
            </div>
        );
    };

    return (
        <Card title="MY ORDER HISTORY" className="product-card">
            <ConfirmPopup />

            <DataTable<OrderDTO[]>
                value={orders}
                loading={loading}
                stripedRows
                expandedRows={expandedRows}
                onRowToggle={(e) => setExpandedRows(e.data)}
                rowExpansionTemplate={rowExpansionTemplate}
                dataKey="id"
                emptyMessage="NO DUCKS FOUND MATCHING YOUR CRITERIA. 🦆"
            >
                <Column expander={true} style={{ width: '3rem' }} />
                <Column field="id" header="ORDER ID" sortable />
                <Column header="DATE" body={dateBodyTemplate} sortable />
                <Column header="STATUS" body={statusBodyTemplate} sortable />
                <Column header="TOTAL PRICE" body={priceBodyTemplate} sortable />
                <Column
                    header="ITEMS"
                    body={(order: OrderDTO) => order.items.reduce((sum, item) => sum + item.quantity, 0)}
                />
                <Column
                    header="ACTIONS"
                    body={(order: OrderDTO) => (
                        <Button
                            onClick={(e) => confirmDelete(e, order.id)}
                            className={`${styles.btn} ${styles.btn_grey}`}
                            icon="pi pi-trash"
                        />
                    )}
                />
            </DataTable>
        </Card>
    );
};

export default OrderHistoryComponent;