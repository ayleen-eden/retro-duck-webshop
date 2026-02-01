import React, {useEffect, useState} from "react";
import {Card} from "primereact/card";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import {Button} from "primereact/button";
import {SubscriptionDTO} from "../DTO/subscription.types";
import {getAllSubscriptionsForUser, unsubscribe} from "../utilities/subscriptionApi";
import {UserxApi} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import styles from "../styles/PixelButton.module.css";
import {dummySubscriptions} from "./DebugSubscriptions";
import {ProductCell} from "./ProductCell";
import {ConfirmPopup, confirmPopup} from "primereact/confirmpopup";
import {Checkbox} from "primereact/checkbox";
import {Splitter, SplitterPanel} from "primereact/splitter";
import {NotificationChannelType} from "../DTO/notification.types";
import {Divider} from "primereact/divider";

/**
 * Subscription management page.
 */
export const SubscriptionComponent: React.FC = () => {

    const USE_DUMMY = false;

    const [subscriptions, setSubscriptions] = useState<SubscriptionDTO[]>(USE_DUMMY ? dummySubscriptions : []);
    const [loading, setLoading] = useState(true);
    const [user, setUser] = useState<UserxTypes | null>(null);
    const [channels, setChannels] = useState<NotificationChannelType[]>([])

    useEffect(() => {

        if (USE_DUMMY) {
            setLoading(false);
            return;
        }

        /**
         * Handles loading state and API errors.
         */
        const loadEverything = async () => {
            try {
                const currentUser = await UserxApi.getCurrentUser();
                setUser(currentUser);
                setChannels(currentUser.preferredChannels ?? [])

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

    /**
     * Unsubscribes the user from a product.
     *
     * @param productId - ID of the product to unsubscribe from
     */
    const handleUnsubscribe = async (productId: number) => {
        if (!user?.id) return;
        await unsubscribe(productId);
        setSubscriptions(prev => prev.filter(sub => sub.productId !== productId));
    };

    /**
     * Opens a confirmation popup before unsubscribing.
     *
     * If accepted, triggers handleUnsubscribe().
     *
     * @param event - Mouse event used to anchor the popup
     * @param item - Product ID of the subscription to remove
     */
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

    /**
     * Renders the unsubscribe action button for each subscription row.
     *
     * @param rowData - Subscription represented by the row
     * @returns TSX unsubscribe button
     */
    const actionBodyTemplate = (rowData: SubscriptionDTO) => (
        <Button
            label="UNSUBSCRIBE"
            icon="pi pi-bell-slash"
            className={`${styles.btn} ${styles.btn_red}`}
            onClick={(event) => confirmDelete(event, rowData.productId)}
        />
    );

    /**
     * Toggles a notification channel preference for the current user.
     *
     * @param type - Notification channel type to toggle
     */
    const toggleChannel = async (type: NotificationChannelType) => {
        if (!user) return;

        const updatedChannels = channels.includes(type)
            ? channels.filter(c => c !== type)
            : [...channels, type];

        setChannels(updatedChannels);

        try {
            await UserxApi.updateCurrentUserProfile({
                ...user,
                preferredChannels: updatedChannels
            });
        } catch (err) {
            console.error("Failed to update channels", err);
        }
    };

    if (loading) return (
        <Card title="MY SUBSCRIPTIONS" className="product-card">
            <i className="pi pi-spin pi-spinner" style={{ fontSize: '5rem' }}></i>
            <h1>LOADING...</h1>
        </Card>
    )

    return (
        <div>
            <Card title="NOTIFICATION CHANNELS" className="product-card">
                <div className="flex align-items-center" style={{marginBottom: '1rem'}}>
                    <Checkbox checked={channels.includes(NotificationChannelType.SMS)} style={{marginRight: '0.5rem'}} className="pixel-checkbox" onChange={() => toggleChannel(NotificationChannelType.SMS)}/>
                    <label>SMS</label>
                </div>
                <div className="flex align-items-center" style={{marginBottom: '1rem'}}>
                    <Checkbox checked={channels.includes(NotificationChannelType.WHATSAPP)} style={{marginRight: '0.5rem'}} className="pixel-checkbox" onChange={() => toggleChannel(NotificationChannelType.WHATSAPP)}/>
                    <label>WHATSAPP</label>
                </div>
                <div className="flex align-items-center">
                    <Checkbox checked={channels.includes(NotificationChannelType.EMAIL)} style={{marginRight: '0.5rem'}} className="pixel-checkbox" onChange={() => toggleChannel(NotificationChannelType.EMAIL)}/>
                    <label>EMAIL</label>
                </div>
            </Card>
            <Divider className="pixel-divider-dashed"/>
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
        </div>
    );
};
