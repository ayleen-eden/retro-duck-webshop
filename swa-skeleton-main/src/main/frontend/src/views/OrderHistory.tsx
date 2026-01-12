import React, { useEffect, useState } from 'react';
import { DataTable } from 'primereact/datatable';
import { Column } from 'primereact/column';
import { Card } from 'primereact/card';
import { Tag } from 'primereact/tag';
import NavbarComponent from "../components/NavbarComponent";
import { FooterComponent } from "../components/FooterComponent";
import { OrderApi } from "../utilities/orderApi";
import { OrderDTO, OrderStatus } from "../DTO/order.types";
import {Button} from "primereact/button";
import styles from "../components/PixelButton.module.css"

const OrderHistory: React.FC = () => {
    const [orders, setOrders] = useState<OrderDTO[]>([]);
    const [loading, setLoading] = useState<boolean>(true);

    useEffect(() => {
        const loadOrders = async () => {
            try {
                const data = await OrderApi.fetchOrderHistory();
                setOrders(data);
            } catch (error) {
                console.error("Error loading orders", error);
            } finally {
                setLoading(false);
            }
        };
        loadOrders();
    }, []);

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

    const deleteOrder = async (orderId: number) => {
        if (window.confirm("Are you sure you want to delete this order?")) {
            try {
                await OrderApi.deleteOrder(orderId);
                setOrders(prevOrders => prevOrders.filter(o => o.id !== orderId));
                alert("Order deleted successfully.");
            } catch (error) {
                console.error("Error deleting order:", error);
                alert("Order could not be deleted.");
            }
        }
    };

    return (
        <div>
            <NavbarComponent />
            <Card title="MY ORDER HISTORY" className="product-card-interactive">
                <DataTable<OrderDTO[]> value={orders} loading={loading} stripedRows emptyMessage="No orders found.">
                    <Column field="id" header="Order ID" sortable />
                    <Column header="Date" body={dateBodyTemplate} sortable />
                    <Column header="Status" body={statusBodyTemplate} sortable />
                    <Column header="Total Price" body={priceBodyTemplate} sortable />
                    <Column
                        header="Items"
                        body={(order: OrderDTO) => order.items.reduce((sum, item) => sum + item.quantity, 0)}
                    />
                    <Column
                        header="Actions"
                        body={(order: OrderDTO) => (
                            <Button
                                onClick={() => deleteOrder(order.id)}
                                className={`${styles.btn} ${styles.btn_grey}`}
                                icon="pi pi-trash"
                            />
                        )}
                    />
                </DataTable>
            </Card>
            <FooterComponent />
        </div>
    );
};

export default OrderHistory;