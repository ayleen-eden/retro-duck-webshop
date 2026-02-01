import React, {useEffect, useState} from "react";
import {NotificationDTO} from "../DTO/notification.types";
import {getAllNotificationsForUser} from "../utilities/notificationApi";
import {UserxApi} from "../utilities/userxApi";
import {Card} from "primereact/card";
import {UserxTypes} from "../DTO/userx.types";
import {DataTable} from "primereact/datatable";
import {Column} from "primereact/column";
import styles from "../styles/PixelButton.module.css";
import {Button} from "primereact/button";
import {dummyNotifications} from "./DebugNotification";
import {getProductById} from "../utilities/productApi";
import {ProductDTO} from "../DTO/product.types";
import {Dialog} from "primereact/dialog";
import {FilterMatchMode} from "primereact/api";
import {Dropdown} from "primereact/dropdown";
import {ProductCell} from "./ProductCell";

/**
 * Notification page component.
 */
export const NotificationComponent: React.FC = () => {

    /**
     * Active DataTable filters.
     * Currently supports filtering notifications by type.
     */
    const [filters, setFilters] = useState({
        type: { value: null, matchMode: FilterMatchMode.EQUALS }
    });

    const USE_DUMMY = false;

    const [user, setUser] = useState<UserxTypes | null>(null);

    const [notifications, setNotifications] = useState<NotificationDTO[]>(USE_DUMMY ? dummyNotifications : []);

    const [loading, setLoading] = useState<boolean>(true);

    /**
     * Currently selected notification for detail view.
     * Used to populate the dialog content.
     */
    const [selectedNotification, setSelectedNotification] = useState<NotificationDTO | null>(null);
    const [detailsVisible, setDetailsVisible] = useState(false);

    /**
     * Available notification types used for filtering.
     */
    const [types] = useState([
        { name: "SALE", code: "SALE" },
        { name: "OUT_OF_STOCK", code: "OUT_OF_STOCK" },
        { name: "RESTOCK", code: "RESTOCK" },
    ]);

    useEffect(() => {

        if (USE_DUMMY) {
            setLoading(false);
            return;
        }

        /**
         * Loads current user and their associated notifications.
         *
         * Sets loading state appropriately during execution.
         */
        const loadEverything = async () => {
            try {
                const currentUser = await UserxApi.getCurrentUser();
                setUser(currentUser);

                if (currentUser.id) {
                    const data = await getAllNotificationsForUser(currentUser.id);
                    setNotifications(data);
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
     * Renders the notification title column.
     *
     * @param rowData - The notification represented by the row
     * @returns Notification title string
     */
    const titleTemplate = (rowData: NotificationDTO) => rowData.title;

    /**
     * Formats and renders the notification timestamp.
     *
     * @param rowData - The notification represented by the row
     * @returns Formatted date string
     */
    const timestampTemplate = (rowData: NotificationDTO) => new Date(rowData.timestamp).toDateString();

    /**
     * Renders the "Details" button for a notification.
     *
     * @param rowData - The notification represented by the row
     * @returns TSX element containing the details button
     */
    const descriptionBodyTemplate = (rowData: NotificationDTO)=> {
        return (
            <div>
                <Button className={`${styles.btn} ${styles.btn_blue}`} icon="pi pi-info-circle" label="DETAILS"
                    onClick={() => { setSelectedNotification(rowData); setDetailsVisible(true);}}/>
            </div>
        )
    }

    /**
     * Renders the notification type column.
     *
     * @param rowData - The notification represented by the row
     * @returns Notification type string
     */
    const typeTemplate = (rowData: NotificationDTO) => rowData.type;

    /**
     * Custom dropdown filter for the notification type column.
     *
     * @param options - PrimeReact filter options object
     * @returns JSX dropdown component for filtering
     */
    const typeFilterTemplate = (options: any) => {
        return (
            <Dropdown
                value={options.value}
                options={types}
                onChange={(e) => options.filterApplyCallback(e.value)}
                optionLabel="name"
                optionValue="code"
                placeholder="ANY"
                className="pixel-dropdown"
                style={{ minWidth: "14rem" }}
                showClear
            />
        );
    };

    if (loading) return (
        <Card title="NOTIFICATIONS" className="product-card">
            <i className="pi pi-spin pi-spinner" style={{ fontSize: '5rem' }}></i>
            <h1>LOADING...</h1>
        </Card>
    )

    return (
        <Card title="NOTIFICATIONS" className="product-card">

            <DataTable<NotificationDTO[]> value={notifications} stripedRows emptyMessage="NO DUCKS FOUND MATCHING YOUR CRITERIA. 🦆" filterDisplay="row" filters={filters} globalFilterFields={['type']}>
                <Column field="title" header="TITLE" body={titleTemplate} sortable/>
                <Column header="PRODUCT" body={(rowData: NotificationDTO) => <ProductCell productId={rowData.productId} />}/>
                <Column header="TYPE" body={typeTemplate} showClearButton={false} showFilterMenu={false} field="type" filter filterElement={typeFilterTemplate} filterPlaceholder="SEARCH BY TYPE"/>
                <Column field="timestamp" header="TIMESTAMP" body={timestampTemplate} sortable/>
                <Column header="DETAILS" body={descriptionBodyTemplate}/>
            </DataTable>

            {/**
            * Dialog displaying the full notification description.
            *
            * Visible when a notification is selected via the Details button.
            */}
            <Dialog
                header="DETAILS"
                visible={detailsVisible}
                style={{ width: '50vw' }}
                onHide={() => setDetailsVisible(false)}
                className="product-card"
            >
                <p>{selectedNotification?.description}</p>
            </Dialog>
        </Card>
    )
}