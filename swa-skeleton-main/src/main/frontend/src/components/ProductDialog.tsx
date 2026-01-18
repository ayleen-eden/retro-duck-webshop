/**
 * Responsible for the UI-dialog when a user (role: manager) creates/edits a product
 */
import React, {useEffect, useState} from 'react';
import {Dialog} from 'primereact/dialog';
import {Button} from "primereact/button";
import {InputText} from "primereact/inputtext";
import {InputNumber, InputNumberValueChangeEvent} from "primereact/inputnumber";
import {InputTextarea} from "primereact/inputtextarea";
import {MultiSelect, MultiSelectChangeEvent} from 'primereact/multiselect';
import {ProductDTO} from "../DTO/product.types";
import {Message} from "primereact/message";
import {ProductApi} from "../utilities/productApi";
import styles from "./PixelButton.module.css";
import '../styles/Login.css';

interface ProductDialogProps {
    visible: boolean;
    product: ProductDTO | null;
    isNewProduct: boolean;
    onHide: () => void;
    onSubmit: (product: any) => void;
}

const ProductDialog: React.FC<ProductDialogProps> = ({
                                                         visible,
                                                         product,
                                                         isNewProduct,
                                                         onHide,
                                                         onSubmit
                                                     }) => {
    const [name, setName] = useState('');
    const [description, setDescription] = useState('');
    const [price, setPrice] = useState<number | null>(0);
    const [stock, setStock] = useState<number | null>(0);
    const [discount, setDiscount] = useState<number | null>(0);
    const [imageUrl, setImageUrl] = useState('');
    const [selectedCategories, setSelectedCategories] = useState<string[]>([]);
    const [categoryOptions, setCategoryOptions] = useState<{ label: string, value: string }[]>([]);
    const [error, setError] = useState<string | null>(null);

    useEffect(() => {
        const loadCategories = async () => {
            try {
                const cats = await ProductApi.fetchCategories();
                setCategoryOptions(cats.map(c => ({label: c, value: c})));
            } catch (e) {
                console.error("Could not fetch categories", e);
            }
        };
        void loadCategories();
    }, []);

    useEffect(() => {
        if (visible) {
            if (product && !isNewProduct) {
                setName(product.name || '');
                setDescription(product.description || '');
                setPrice(product.price);
                setStock(product.stock);
                setDiscount(product.discount || 0);
                setImageUrl(product.imageUrl || '');
                setSelectedCategories(product.categories || []);
            } else {
                setName('');
                setDescription('');
                setPrice(0);
                setStock(0);
                setDiscount(0);
                setImageUrl('');
                setSelectedCategories([]);
            }
            setError(null);
        }
    }, [visible, product, isNewProduct]);

    const handleFormSubmit = () => {
        if (!name.trim()) {
            setError("Name is required.");
            return;
        }
        if (price === null) {
            setError("Price is required.");
            return;
        }

        const payload = {
            id: isNewProduct ? undefined : product?.id,
            name,
            description,
            price,
            stock: stock || 0,
            discount: discount || 0,
            imageUrl,
            categories: selectedCategories
        };

        onSubmit(payload);
    };

    return (
        <Dialog
            header={isNewProduct ? "New Product" : `Edit: ${product?.name}`}
            visible={visible}
            style={{width: '50vw'}}
            onHide={onHide}
            footer={(
                <div className="pt-3">
                    <Button label="Cancel" icon="pi pi-times" onClick={onHide} className="p-button-text"/>
                    <Button
                        label={isNewProduct ? "Create Product" : "Save Changes"}
                        icon="pi pi-check"
                        onClick={handleFormSubmit}
                        className={`${styles.btn} ${styles.btn_green}`}
                    />
                </div>
            )}
            className="product-card"
        >
            {error && <Message severity="error" text={error} className="mb-3 w-full"/>}

            <div className="flex flex-column gap-4 mt-2">
                <div className="flex flex-column gap-2">
                    <label htmlFor="name" className="font-bold">Product Name</label>
                    <InputText id="name" value={name} onChange={(e) => setName(e.target.value)}
                               className="input-field"/>
                </div>

                <div className="flex flex-column gap-2">
                    <label htmlFor="description" className="font-bold">Description</label>
                    <InputTextarea id="description" value={description} onChange={(e) => setDescription(e.target.value)}
                                   rows={3} className="input-field" autoResize/>
                </div>

                <div className="flex flex-wrap gap-3">
                    <div className="flex-1 flex flex-column gap-2" style={{minWidth: '150px'}}>
                        <label htmlFor="price" className="font-bold">Price (€)</label>
                        <InputNumber id="price" value={price}
                                     onValueChange={(e: InputNumberValueChangeEvent) => setPrice(e.value ?? null)}
                                     mode="currency" currency="EUR" locale="de-DE" inputClassName="input-field w-full"/>
                    </div>
                    <div className="flex-1 flex flex-column gap-2" style={{minWidth: '150px'}}>
                        <label htmlFor="stock" className="font-bold">Initial Stock</label>
                        <InputNumber id="stock" value={stock}
                                     onValueChange={(e: InputNumberValueChangeEvent) => setStock(e.value ?? null)}
                                     inputClassName="input-field w-full"/>
                    </div>
                    <div className="flex-1 flex flex-column gap-2" style={{minWidth: '150px'}}>
                        <label htmlFor="discount" className="font-bold">Discount (0-1)</label>
                        <InputNumber id="discount" value={discount}
                                     onValueChange={(e: InputNumberValueChangeEvent) => setDiscount(e.value ?? null)}
                                     min={0} max={1} minFractionDigits={2} inputClassName="input-field w-full"/>
                    </div>
                </div>

                <div className="flex flex-column gap-2">
                    <label htmlFor="categories" className="font-bold">Categories</label>
                    <MultiSelect
                        id="categories"
                        value={selectedCategories}
                        options={categoryOptions}
                        onChange={(e: MultiSelectChangeEvent) => setSelectedCategories(e.value)}
                        optionLabel="label"
                        placeholder="Select Categories"
                        display="chip"
                        className="pixel-multiselect w-full"
                    />
                </div>

                <div className="flex flex-column gap-2">
                    <label htmlFor="imageUrl" className="font-bold">Image URL</label>
                    <InputText id="imageUrl" value={imageUrl} onChange={(e) => setImageUrl(e.target.value)}
                               className="input-field" placeholder="e.g. /images/duck.png"/>
                </div>
            </div>
        </Dialog>
    );
};

export default ProductDialog;