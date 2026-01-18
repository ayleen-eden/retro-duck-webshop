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
import styles from "../components/PixelButton.module.css";

const ManageProducts: React.FC = () => {
    const [products, setProducts] = useState<ProductDTO[]>([]);
    const [loading, setLoading] = useState(true);
    const [dialogVisible, setDialogVisible] = useState(false);
    const [selectedProduct, setSelectedProduct] = useState<ProductDTO | null>(null);
    const [isNewProduct, setIsNewProduct] = useState(false);
    const [globalFilter, setGlobalFilter] = useState<string>('');

    const toast = useRef<Toast>(null);

    // --- API CALLS ---
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

    useEffect(() => {
        loadProducts();
    }, []);

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
    const openNew = () => {
        setSelectedProduct(null);
        setIsNewProduct(true);
        setDialogVisible(true);
    };

    const openEdit = (product: ProductDTO) => {
        setSelectedProduct(product);
        setIsNewProduct(false);
        setDialogVisible(true);
    };

    const confirmDelete = (event: React.MouseEvent<HTMLButtonElement>, product: ProductDTO) => {
        confirmPopup({
            target: event.currentTarget,
            message: `Are you sure you want to delete ${product.name}?`,
            icon: 'pi pi-exclamation-triangle',
            accept: () => deleteProduct(product.id),
            className: "pixel-confirmpopup"
        });
    };

    // --- TEMPLATES ---
    const imageBodyTemplate = (rowData: ProductDTO) => {
        return <img src={rowData.imageUrl || '/images/duck.png'} alt={rowData.name}
                    style={{width: '50px', height: '50px', objectFit: 'contain'}}
                    onError={(e) => (e.currentTarget.src = '/images/duck.png')}/>;
    };

    const priceBodyTemplate = (rowData: ProductDTO) => `${rowData.price.toFixed(2)} €`;

    const stockBodyTemplate = (rowData: ProductDTO) => (
        <Tag value={rowData.stock} severity={rowData.stock > 0 ? 'success' : 'danger'}
             className={rowData.stock > 0 ? 'pixel-tag pixel-tag-green' : 'pixel-tag pixel-tag-red'}/>
    );

    const actionBodyTemplate = (rowData: ProductDTO) => (
        <div>
            <Button icon="pi pi-pencil" className={`${styles.btn} ${styles.btn_yellow} mr-2`}
                    onClick={() => openEdit(rowData)} tooltip="Edit"/>
            <Button icon="pi pi-trash" className={`${styles.btn} ${styles.btn_red}`}
                    onClick={(e) => confirmDelete(e, rowData)} tooltip="Delete"/>
        </div>
    );

    // --- TABLE HEADER ---
    const renderHeader = () => {
        return (
            <div className="flex flex-wrap gap-2 align-items-center justify-content-between">
                <Button
                    label="New Product"
                    icon="pi pi-plus"
                    className={`${styles.btn} ${styles.btn_green}`}
                    onClick={openNew}
                />

                <IconField iconPosition="left">
                    <InputIcon className="pi pi-search"/>
                    <InputText
                        type="search"
                        onInput={(e) => setGlobalFilter(e.currentTarget.value)}
                        placeholder="Search products..."
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
                <Card title="Prouct management" className="product-card">

                    <DataTable
                        value={products}
                        loading={loading}
                        stripedRows
                        paginator
                        rows={10}
                        rowsPerPageOptions={[5, 10, 25, 50]}
                        globalFilter={globalFilter}
                        header={header}
                        globalFilterFields={['name', 'id', 'description', 'categories']}
                        emptyMessage="No products found."
                    >
                        <Column field="id" header="ID" sortable style={{width: '5%'}}/>
                        <Column header="Image" body={imageBodyTemplate} style={{width: '10%'}}/>
                        <Column field="name" header="Name" sortable style={{width: '25%'}}/>
                        <Column field="price" header="Price" body={priceBodyTemplate} sortable style={{width: '10%'}}/>
                        <Column field="stock" header="Stock" body={stockBodyTemplate} sortable style={{width: '10%'}}/>
                        <Column field="discount" header="Discount" sortable style={{width: '10%'}}/>
                        <Column header="Actions" body={actionBodyTemplate} style={{width: '15%'}}/>
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