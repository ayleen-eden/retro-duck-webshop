import React, { useEffect, useState } from "react";
import { Card } from "primereact/card";
import { DataTable } from "primereact/datatable";
import { Column } from "primereact/column";
import { Button } from "primereact/button";
import { SubscriptionDTO } from "../DTO/subscription.types";
import { getAllSubscriptionsForUser, unsubscribe } from "../utilities/subscriptionApi";
import { UserxApi } from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import styles from "./PixelButton.module.css";
import {dummySubscriptions} from "./DebugSubscriptions";
import {ProductCell} from "./ProductCell";
import { ConfirmPopup, confirmPopup } from "primereact/confirmpopup";

export const SubscriptionComponent: React.FC = () => {

    const USE_DUMMY = false;

    const [subscriptions, setSubscriptions] = useState<SubscriptionDTO[]>(USE_DUMMY ? dummySubscriptions : []);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState<UserxTypes | null>(null);

    useEffect(() => {

        if (USE_DUMMY) {
            setLoading(false);
            return;
        }

        const loadEverything = async () => {
            try {
                const currentUser = await UserxApi.getCurrentUser();
                setUser(currentUser);

                if (currentUser.id) {
                    const data = await getAllSubscriptionsForUser(currentUser.id);
                    setSubscriptions(data);
                }
            } catch (error) {
                console.error("Error:", error);
            } finally {
                setLoading(false);
            }
        };

        loadEverything();
    }, []);

    const handleUnsubscribe = async (productId: number) => {
        if (!user?.id) return;
        await unsubscribe(user.id, productId);
        setSubscriptions(prev => prev.filter(sub => sub.productId !== productId));
    };

    const confirmDelete = (event: React.MouseEvent<HTMLButtonElement>, item: number) => {
        confirmPopup({
            className:"pixel-confirmpopup pixel-icon",
            target: event.currentTarget,
            message:`ARE YOU SURE YOU WANT TO UNSUBSCRIBE?`,
            icon:"pi pi-exclamation-triangle",
            acceptIcon:"pi pi-check",
            rejectIcon:"pi pi-times",
            accept: () => handleUnsubscribe(item),
            reject: () => {}
        });
    };

    const actionBodyTemplate = (rowData: SubscriptionDTO) => (
        <Button
            label="UNSUBSCRIBE"
            icon="pi pi-bell-slash"
            className={`${styles.btn} ${styles.btn_red}`}
            onClick={(event) => confirmDelete(event, rowData.productId)}
        />
    );

    if (loading) return (
        <Card title="MY SUBSCRIPTIONS" className="product-card">
            <i className="pi pi-spin pi-spinner" style={{ fontSize: '5rem' }}></i>
            <h1>LOADING...</h1>
        </Card>
    )

    return (
        <Card title="MY SUBSCRIPTIONS" className="product-card">
            <ConfirmPopup/>
            <DataTable
                value={subscriptions}
                stripedRows
                emptyMessage="YOU HAVE NO SUBSCRIPTIONS."
            >
                <Column header="PRODUCT" body={(rowData: SubscriptionDTO) => <ProductCell productId={rowData.productId} />}/>
                <Column header="ACTION" body={actionBodyTemplate} />
            </DataTable>
        </Card>
    );
};
