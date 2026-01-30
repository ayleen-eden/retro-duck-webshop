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

export const NotificationComponent: React.FC = () => {

    const [filters, setFilters] = useState({
        type: { value: null, matchMode: FilterMatchMode.EQUALS }
    });

    const USE_DUMMY = false;

    const [user, setUser] = useState<UserxTypes | null>(null);
    const [notifications, setNotifications] = useState<NotificationDTO[]>(USE_DUMMY ? dummyNotifications : []);
    const [loading, setLoading] = useState<boolean>(true);
    const [selectedNotification, setSelectedNotification] = useState<NotificationDTO | null>(null);
    const [detailsVisible, setDetailsVisible] = useState(false);
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

    const titleTemplate = (rowData: NotificationDTO) => rowData.title;

    const timestampTemplate = (rowData: NotificationDTO) => new Date(rowData.timestamp).toDateString();
    const descriptionBodyTemplate = (rowData: NotificationDTO)=> {
        return (
            <div>
                <Button className={`${styles.btn} ${styles.btn_blue}`} icon="pi pi-info-circle" label="DETAILS"
                    onClick={() => { setSelectedNotification(rowData); setDetailsVisible(true);}}/>
            </div>
        )
    }
    const typeTemplate = (rowData: NotificationDTO) => rowData.type;

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