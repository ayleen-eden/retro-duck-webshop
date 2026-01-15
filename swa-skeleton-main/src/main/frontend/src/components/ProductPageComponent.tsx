import React, {useState, useEffect} from "react";
import {useParams} from "react-router-dom";
import {dummyProduct1} from "./DebugProducts"; // Fallback
import {ProductDTO} from "../DTO/product.types";
import {Card} from "primereact/card";
import {Button} from "primereact/button";
import {useSessionStorage} from "primereact/hooks";
import {Tag} from "primereact/tag";
import {Rating} from "primereact/rating";
import RatingComponent from "./RatingComponent";
import {ProgressSpinner} from 'primereact/progressspinner';
import {addToCart, getCart} from "../utilities/cartUtilities";

const ProductPageComponent: React.FC = () => {
    // Get ID from URL
    const {productId} = useParams<{ productId: string }>();

    const [product, setProduct] = useState<ProductDTO | null>(null); // Startet leer
    const [loading, setLoading] = useState<boolean>(true);

    const [quantity, setQuantity] = useSessionStorage<number>(1, 'quantity');

    useEffect(() => {
        if (!productId) return;

        setLoading(true);
        fetch(`/api/products/${productId}`)
            .then(response => {
                if (!response.ok) {
                    throw new Error('Product not found');
                }
                return response.json();
            })
            .then((data: ProductDTO) => {
                console.log("Loaded Product Details:", data);
                setProduct(data);
                setLoading(false);
            })
            .catch(error => {
                console.error("Error loading product details:", error);
                setProduct(dummyProduct1);
                setLoading(false);
            });
    }, [productId]);

    if (loading) {
        return (
            <div className="flex justify-content-center align-items-center" style={{height: '50vh'}}>
                <ProgressSpinner/>
            </div>
        );
    }

    if (!product) {
        return <div>Product not found.</div>;
    }

    const currentPrice = product.price;
    const hasDiscount = product.discount > 0;
    const discountedPrice = (currentPrice * (1 - product.discount)).toFixed(2);

    return (
        <div className="p-4">
            <Card>
                <div style={{display: 'flex', gap: '2rem', flexWrap: 'wrap'}}>
                    <div style={{
                        flex: '0 0 auto',
                        display: 'flex',
                        justifyContent: 'center',
                        width: '100%',
                        maxWidth: '500px'
                    }}>
                        <img
                            style={{maxWidth: '100%', height: 'auto', maxHeight: '500px', objectFit: 'contain'}}
                            src={product.imageUrl || "/images/duck.png"}
                            alt={product.name}
                            onError={(e) => (e.currentTarget.src = '/images/duck.png')}
                        />
                    </div>

                    <div style={{flex: 1}}>
                        <h1>{product.name}</h1>

                        <div className="mb-3">
                            {/* Fixed Rating */}
                            <Rating value={5} readOnly cancel={false}/>
                        </div>

                        <p className="mb-4 text-lg">{product.description}</p>

                        <div className="mb-4">
                            {hasDiscount ? (
                                <>
                                    <span style={{
                                        textDecoration: 'line-through',
                                        color: '#999',
                                        marginRight: '1rem',
                                        fontSize: '1.2rem'
                                    }}>
                                        {currentPrice.toFixed(2)} €
                                    </span>
                                    <span style={{color: "var(--red-500)", fontSize: '2rem', fontWeight: 'bold'}}>
                                        {discountedPrice} €
                                    </span>
                                    <Tag value={`-${(product.discount * 100).toFixed(0)}% Sale`} severity="warning"
                                         className="ml-2"/>
                                </>
                            ) : (
                                <span style={{fontSize: '2rem', fontWeight: 'bold'}}>
                                    {currentPrice.toFixed(2)} €
                                </span>
                            )}
                        </div>

                        <div style={{display: 'flex', flexDirection: 'column', gap: '1rem'}}>
                            {/* Choose amount */}
                            <div style={{display: 'flex', gap: '1rem', alignItems: 'center'}}>
                                <Button
                                    icon="pi pi-minus"
                                    className="p-button-rounded p-button-outlined"
                                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                                    disabled={product.stock <= 0}
                                />
                                <span className="font-bold text-2xl" style={{minWidth: '3rem', textAlign: 'center'}}>
                                    {quantity}
                                </span>
                                <Button
                                    icon="pi pi-plus"
                                    className="p-button-rounded p-button-outlined"
                                    onClick={() => setQuantity(quantity + 1)}
                                    disabled={product.stock <= 0}
                                />
                            </div>

                            {/* Stock Info */}
                            <div>
                                {product.stock > 0 ? (
                                    <span className="text-green-500 font-bold"><i
                                        className="pi pi-check"></i> {product.stock} in stock</span>
                                ) : (
                                    <span className="text-red-500 font-bold"><i className="pi pi-times"></i> Out of stock</span>
                                )}
                            </div>

                            {/* Actions */}
                            <div style={{display: 'flex', gap: '1rem', flexWrap: 'wrap'}}>
                                <Button
                                    label={product.stock > 0 ? "Add to Cart" : "Sold Out"}
                                    icon="pi pi-cart-plus"
                                    disabled={product.stock <= 0}
                                    size="large"
                                    onClick={ () => addToCart(getCart(), product, quantity) }
                                />
                                <Button
                                    icon="pi pi-bell"
                                    label="Notify me"
                                    className="p-button-warning p-button-outlined"
                                    size="large"
                                />
                            </div>
                        </div>

                        <div className="mt-4 flex gap-2">
                            {product.categories && product.categories.map(cat => (
                                <Tag key={cat} value={cat} severity="info"/>
                            ))}
                        </div>
                    </div>
                </div>
            </Card>

            <div className="mt-4">
                <RatingComponent productId={Number(productId)}/>
            </div>
        </div>
    );
};

export default ProductPageComponent;