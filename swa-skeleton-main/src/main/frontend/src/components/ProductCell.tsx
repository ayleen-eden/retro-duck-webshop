import React, {useEffect, useState} from "react";
import {ProductDTO} from "../DTO/product.types";
import {getProductById} from "../utilities/productApi";
import {Button} from "primereact/button";
import styles from "../styles/PixelButton.module.css";

export const ProductCell: React.FC<{ productId: number }> = ({ productId }) => {
    const [product, setProduct] = useState<ProductDTO | null>(null);

    useEffect(() => {
        const fetchProduct = async () => {
            try {
                const data = await getProductById(productId);
                setProduct(data);
            } catch (err) {
                console.error("Product fetch failed:", err);
            }
        };
        fetchProduct();
    }, [productId]);

    if (!product) return <span>Loading product... ⏳</span>;

    return (
        <div>
            <p><strong>{product.name}</strong></p>
            <Button
                className={`${styles.btn} ${styles.btn_yellow}`}
                icon="pi pi-external-link"
                label="SEE PRODUCT"
                onClick={() => window.location.href = `/products/${productId}`}
            />
        </div>
    );
};