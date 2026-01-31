import React, { useEffect, useState } from 'react';
import {DataTable} from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';
import { OrderApi } from "../utilities/orderApi";
import {OrderDTO, OrderItemDTO, OrderStatus} from "../DTO/order.types";
import {Button} from "primereact/button";
import styles from "../styles/PixelButton.module.css"
import {ConfirmPopup, confirmPopup} from "primereact/confirmpopup";

/**
 * Component for displaying the authenticated user's order history.
 * * * This component fetches all previous orders from the {@link OrderApi}.
 * * It features an expandable table to show individual items per order.
 * * It handles order deletion, accounting for backend constraints like
 * {@code OrderNotFoundException} or {@code UnauthorizedOrderAccessException}.
 */
const OrderHistoryComponent: React.FC = () => {
    const [orders, setOrders] = useState<OrderDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true);
    const [expandedRows, setExpandedRows] = useState<any>(null);

    useEffect(() => {
        loadOrders();
    }, []);

    /**
     * Fetches the order history from the server and updates the state.
     */
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

    /**
     * Triggers a confirmation popup before calling the delete API.
     * @param event - The click event to anchor the popup.
     * @param orderId - The ID of the order to be deleted.
     */
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

    /**
     * Executes the deletion via API and refreshes the local list.
     * @param orderId - ID of the order.
     */
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
            rowData.status === OrderStatus.NEW ? 'danger' : 'info';
        return <Tag className="pixel-tag pixel-tag-blue" value={rowData.status} severity={severity} />;
    };

    const dateBodyTemplate = (rowData: OrderDTO) => {
        return new Date(rowData.orderDate).toLocaleString();
    };

    const priceBodyTemplate = (rowData: OrderDTO) => {
        return `${rowData.totalPrice.toFixed(2)} €`;
    };

    /**
     * Template for rendering the expanded row details (individual order items).
     * @param data - The OrderDTO for the current row.
     */
    const rowExpansionTemplate = (data: OrderDTO) => {
        return (
            <div style={{ backgroundColor: 'rgba(0,0,0,0.05)', borderRadius: '8px', padding: '1rem' }}>
                <h4 style={{ marginTop: '1rem', marginLeft: '1rem' }}>DETAILS FOR ORDER #{data.id}</h4>
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