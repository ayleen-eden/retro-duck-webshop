import React, {useState, useEffect, useRef} from 'react';
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import {DataTable} from 'primereact/datatable';
import {Column} from 'primereact/column';
import {Button} from 'primereact/button';
import {Card} from 'primereact/card';
import {Toast} from 'primereact/toast';
import {ConfirmPopup, confirmPopup} from 'primereact/confirmpopup';
import {InputText} from 'primereact/inputtext';
import {IconField} from 'primereact/iconfield';
import {InputIcon} from 'primereact/inputicon';
import {Tag} from 'primereact/tag';
import {ProductDTO} from '../DTO/product.types';
import ProductDialog from '../components/ProductDialog';
import {ProductApi} from '../utilities/productApi';
import styles from "../styles/PixelButton.module.css";

/**
 * ManageProducts.
 * <p>
 * This component provides an administrative interface for managing the product catalog.
 * It allows authorized users (Managers/Admins) to view all products in a paginated table,
 * search for specific items, create new products, edit existing ones, and delete items.
 *
 * @component
 */
const ManageProducts: React.FC = () => {
    const [products, setProducts] = useState<ProductDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const [dialogVisible, setDialogVisible] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<ProductDTO | null>(null);
    const [isNewProduct, setIsNewProduct] = useState(false);
    const [globalFilter, setGlobalFilter] = useState<string>('');

    const toast = useRef<Toast>(null);

    // --- API CALLS ---

    /**
     * Fetches the current list of products from the backend.
     * Sets the loading state during the request and handles potential errors via Toast.
     * @async
     */
    const loadProducts = async () => {
        setLoading(true);
        try {
            const data = await ProductApi.getAllProducts();
            setProducts(data);
        } catch (error) {
            console.error(error);
            toast.current?.show({severity: 'error', summary: 'Error', detail: 'Could not load products'});
        } finally {
            setLoading(false);
        }
    };

    /**
     * Effect hook to trigger the initial product load when the component mounts.
     */
    useEffect(() => {
        loadProducts();
    }, []);

    /**
     * Handles the submission of the product form (create or update).
     * <p>
     * Depending on 'isNewProduct', it calls either the create or update API.
     * After a successful operation, it refreshes the product list.
     *
     * @param productData - The product data collected from the ProductDialog.
     * @async
     */
    const saveProduct = async (productData: any) => {
        try {
            if (isNewProduct) {
                await ProductApi.createProduct(productData);
                toast.current?.show({severity: 'success', summary: 'Success', detail: 'Product created'});
            } else {
                await ProductApi.updateProduct(productData.id, productData);
                toast.current?.show({severity: 'success', summary: 'Success', detail: 'Product updated'});
            }
            setDialogVisible(false);
            loadProducts();
        } catch (error) {
            console.error(error);
            toast.current?.show({severity: 'error', summary: 'Error', detail: 'Could not save product'});
        }
    };

    /**
     * Deletes a product from the database and refreshes the view.
     *
     * @param id - The unique identifier of the product to be deleted.
     * @async
     */
    const deleteProduct = async (id: number) => {
        try {
            await ProductApi.deleteProduct(id);
            toast.current?.show({severity: 'success', summary: 'Deleted', detail: 'Product deleted'});
            loadProducts();
        } catch (error) {
            console.error(error);
            toast.current?.show({severity: 'error', summary: 'Error', detail: 'Could not delete product'});
        }
    };

    // --- UI HANDLERS ---

    /**
     * Prepares the UI for creating a new product.
     */
    const openNew = () => {
        setSelectedProduct(null);
        setIsNewProduct(true);
        setDialogVisible(true);
    };

    /**
     * Prepares the UI for editing an existing product.
     * @param product - The product entity to be edited.
     */
    const openEdit = (product: ProductDTO) => {
        setSelectedProduct(product);
        setIsNewProduct(false);
        setDialogVisible(true);
    };

    /**
     * Displays a confirmation popup before executing the deletion of a product.
     *
     * @param event - The mouse event used to anchor the popup.
     * @param product - The product entity targeted for deletion.
     */
    const confirmDelete = (event: React.MouseEvent<HTMLButtonElement>, product: ProductDTO) => {
        confirmPopup({
            target: event.currentTarget,
            message: `ARE YOU SURE YOU WANT TO DELETE ${product.name}?`,
            icon: 'pi pi-exclamation-triangle',
            accept: () => deleteProduct(product.id),
            className: "pixel-confirmpopup"
        });
    };

    // --- TEMPLATES ---

    /** Renders the product image within a table cell. */
    const imageBodyTemplate = (rowData: ProductDTO) => {
        return <img src={rowData.imageUrl || '/images/duck.png'} alt={rowData.name}
                    style={{width: '50px', height: '50px', objectFit: 'contain'}}
                    onError={(e) => (e.currentTarget.src = '/images/duck.png')}/>;
    };

    /** Formats the price for table display. */
    const priceBodyTemplate = (rowData: ProductDTO) => `${rowData.price.toFixed(2)} €`;

    /** Renders a color-coded tag representing the stock level. */
    const stockBodyTemplate = (rowData: ProductDTO) => (
        <Tag value={rowData.stock} severity={rowData.stock > 0 ? 'success' : 'danger'}
             className={rowData.stock > 0 ? 'pixel-tag pixel-tag-green' : 'pixel-tag pixel-tag-red'}/>
    );

    /** Renders the action buttons (Edit/Delete) for each table row. */
    const actionBodyTemplate = (rowData: ProductDTO) => (
        <div>
            <Button icon="pi pi-pencil" className={`${styles.btn} ${styles.btn_yellow} mr-2`}
                    onClick={() => openEdit(rowData)} tooltip="Edit"/>
            <Button icon="pi pi-trash" className={`${styles.btn} ${styles.btn_red}`}
                    onClick={(e) => confirmDelete(e, rowData)} tooltip="Delete"/>
        </div>
    );

    /**
     * Renders the header section of the DataTable, including the global search and "New" button.
     */
    const renderHeader = () => {
        return (
            <div className="flex flex-wrap gap-2 align-items-center justify-content-between">
                <Button
                    label="NEW PRODUCT"
                    icon="pi pi-plus"
                    className={`${styles.btn} ${styles.btn_green}`}
                    onClick={openNew}
                    style={{marginBottom: '1rem'}}
                />

                <IconField iconPosition="left">
                    <InputIcon className="pi pi-search"/>
                    <InputText
                        type="search"
                        onInput={(e) => setGlobalFilter(e.currentTarget.value)}
                        placeholder=" SEARCH PRODUCTS..."
                        className="p-inputtext-sm"
                    />
                </IconField>
            </div>
        );
    };

    const header = renderHeader();

    return (
        <div>
            <NavbarComponent/>
            <Toast ref={toast}/>
            <ConfirmPopup/>

            <div className="p-4" style={{marginBottom: '80px'}}>
                <Card title="PRODUCT MANAGEMENT" className="product-card">

                    <DataTable
                        value={products}
                        loading={loading}
                        stripedRows
                        paginator
                        paginatorClassName="pixel-paginator"
                        rows={10}
                        rowsPerPageOptions={[5, 10, 25, 50]}

                        globalFilter={globalFilter}
                        header={header}
                        globalFilterFields={['name', 'id', 'description', 'categories']}
                        emptyMessage="NO DUCKS FOUND MATCHING YOUR CRITERIA. 🦆"
                    >
                        <Column field="id" header="ID" sortable style={{width: '5%'}}/>
                        <Column header="IMAGE" body={imageBodyTemplate} style={{width: '10%'}}/>
                        <Column field="name" header="NAME" sortable style={{width: '25%'}}/>
                        <Column field="price" header="PRICE" body={priceBodyTemplate} sortable style={{width: '10%'}}/>
                        <Column field="stock" header="STOCK" body={stockBodyTemplate} sortable style={{width: '10%'}}/>
                        <Column field="discount" header="DISCOUNT" sortable style={{width: '10%'}}/>
                        <Column header="ACTIONS" body={actionBodyTemplate} style={{width: '15%'}}/>
                    </DataTable>
                </Card>
            </div>

            <ProductDialog
                visible={dialogVisible}
                product={selectedProduct}
                isNewProduct={isNewProduct}
                onHide={() => setDialogVisible(false)}
                onSubmit={saveProduct}
            />
            <FooterComponent/>
        </div>
    );
};

export default ManageProducts;