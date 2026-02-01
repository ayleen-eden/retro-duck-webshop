import React, {useState, useEffect} from "react";
import {ProductDTO} from "../DTO/product.types";
import {Card} from "primereact/card";
import {Button} from "primereact/button";
import {useSessionStorage} from "primereact/hooks";
import {Tag} from "primereact/tag";
import {ProgressSpinner} from 'primereact/progressspinner';
import {addToCart, getCart} from "../utilities/cartUtilities";
import styles from "../styles/PixelButton.module.css"
import {getAllSubscriptionsForUser, subscribe, unsubscribe} from "../utilities/subscriptionApi";
import {UserxApi} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import {ConfirmPopup, confirmPopup} from "primereact/confirmpopup";
import {ProductApi} from "../utilities/productApi";

interface ProductComponentProps {
    productId: number;
}

const ProductPageComponent: React.FC<ProductComponentProps> = ({productId}) => {
    const [product, setProduct] = useState<ProductDTO | null>(null); // Startet leer
    const [loading, setLoading] = useState<boolean>(true);
    const [quantity, setQuantity] = useSessionStorage<number>(1, 'quantity');
    const [subscribed, setSubscribed] = useState(false);
    const [subLoading, setSubLoading] = useState(false);


    /**
     * Fetches product based on productId from the backend
     */
    useEffect(() => {
        if (!productId) return;
        const loadProductInfo = async () => {
            try {
                const product: ProductDTO = await ProductApi.getProductById(productId);
                setProduct(product);
            } catch (err: any) {
                console.error('Error fetching product:', err);
            } finally {
                setQuantity(1);
                setLoading(false);
            }
        }
        void loadProductInfo();
    }, [productId]);

    /**
     * Loads existing subscription status
     */
    useEffect(() => {
        const loadUserAndSubscription = async () => {
            try {
                const currentUser = await UserxApi.getCurrentUser();
                if (currentUser.id && productId) {
                    const subs = await getAllSubscriptionsForUser(currentUser.id);
                    const isSubscribed = subs.some(sub => sub.productId === Number(productId));
                    setSubscribed(isSubscribed);
                }
            } catch (err) {
                console.error("Failed to load subscription info:", err);
            }
        };
        void loadUserAndSubscription();
    }, [productId]);

    /**
     * Logic for the subscribe toggle
     */
    const handleSubscribeToggle = async () => {
        setSubLoading(true);
        try {
            if (subscribed) {
                await unsubscribe(Number(productId));
                setSubscribed(false);
            } else {
                await subscribe(Number(productId));
                setSubscribed(true);
            }
        } catch (err) {
            console.error("Subscription action failed:", err);
        } finally {
            setSubLoading(false);
        }
    };

    /**
     * Dialog for subscription cancelation
     * @param event Mouse Event when clicked on button
     */
    const confirmSubToggle = (event: React.MouseEvent<HTMLButtonElement>) => {
        if (!subscribed) {
            handleSubscribeToggle();
        } else {
            confirmPopup({
                target: event.currentTarget,
                message: 'ARE YOU SURE YOU WANT TO UNSUBSCRIBE?',
                icon: 'pi pi-exclamation-triangle',
                acceptClassName: 'p-button-danger',
                className: "pixel-confirmpopup",
                accept: () => handleSubscribeToggle(),
            });
        }
    };

    if (loading) {
        return (
            <div className="flex justify-content-center align-items-center" style={{height: '50vh'}}>
                <ProgressSpinner/>
            </div>
        );
    }

    if (!product) {
        return <div>DUCK NOT FOUND.</div>;
    }

    /**
     * Product image rendering
     */
    const imageBox = () => {
        return (
            <div style={{
                flex: '0 0 auto',
                display: 'flex',
                justifyContent: 'center',
            }}>
                <img
                    style={{maxWidth: '100%', height: 'auto', maxHeight: '500px', objectFit: 'contain'}}
                    src={product.imageUrl || "/images/duck.png"}
                    alt={product.name}
                    onError={(e) => (e.currentTarget.src = '/images/duck.png')}
                />
            </div>
        )
    }

    /**
     * Display logic for the price tag and discount
     */
    const priceTag = () => {
        const currentPrice: number = product.price;
        const hasDiscount: boolean = product.discount > 0;
        const discountedPrice: string = (currentPrice * (1 - product.discount)).toFixed(2);
        return (
            <>
                {hasDiscount ? (
                    <>
                        <div className="mb-4" style={{display: 'flex', marginBottom: 10, alignItems: 'center'}}>
                                    <span style={{color: "var(--red-500)", fontSize: '2rem', fontWeight: 'bold'}}>
                                        {discountedPrice} €
                                    </span>

                            <Tag style={{marginLeft: '1rem'}} value={`-${(product.discount * 100).toFixed(0)}% Sale`}
                                 severity="warning"
                                 className="pixel-tag pixel-tag-yellow"/>
                        </div>
                        <span style={{
                            textDecoration: 'line-through',
                            textDecorationThickness: '3px',
                            color: '#999',
                            marginRight: '1rem',
                            fontSize: '1.2rem'
                        }}>
                                        {currentPrice.toFixed(2)} €
                                    </span>
                    </>
                ) : (
                    <span style={{fontSize: '2rem', fontWeight: 'bold', color: 'white'}}>
                                    {currentPrice.toFixed(2)} €
                                </span>
                )}
            </>
        );
    }

    /**
     * Display logic for the product amount + and - buttons
     */
    const amountButtons = () => {
        return (
            <div style={{display: 'flex', gap: '1rem', alignItems: 'center'}}>
                <Button
                    icon="pi pi-minus"
                    className={`${styles.btn} ${styles.btn_red}`}
                    onClick={() => setQuantity(Math.max(1, quantity - 1))}
                    disabled={product.stock <= 0}
                />
                <span className="font-bold text-2xl" style={{minWidth: '3rem', textAlign: 'center', color: 'white'}}>
                                    {quantity}
                                </span>
                <Button
                    icon="pi pi-plus"
                    className={`${styles.btn} ${styles.btn_green}`}
                    onClick={() => setQuantity(quantity + 1)}
                    disabled={product.stock <= 0 || quantity >= product.stock}
                />
            </div>
        )
    }

    /**
     * Display logic for the stock information
     */
    const stockInfo = () => {
        return (
            <div>
                {product.stock > 0 ? (
                    <span className="text-green-500 font-bold"><i
                        className="pi pi-check"></i> {product.stock} IN STOCK</span>
                ) : (
                    <span className="text-red-500 font-bold"><i className="pi pi-times"></i> OUT OF STOCK</span>
                )}
            </div>
        );
    }

    /**
     * Logic for the "Add to cart" and "Notification" button
     */
    const actionButtons = () => {
        return (
            <div style={{display: 'flex', gap: '1rem', flexWrap: 'wrap'}}>
                <Button
                    label={product.stock > 0 ? "ADD TO CART" : "SOLD OUT"}
                    icon="pi pi-cart-plus"
                    disabled={product.stock <= 0}
                    size="large"
                    onClick={() => addToCart(getCart(), product, quantity)}
                    className={`${styles.btn} ${styles.btn_green}`}
                />
                <Button
                    icon={subscribed ? "pi pi-bell-slash" : "pi pi-bell"}
                    label={subscribed ? "UNSUBSCRIBE" : "SUBSCRIBE"}
                    className={`${styles.btn} ${subscribed ? styles.btn_red : styles.btn_yellow}`}
                    disabled={subLoading}
                    size="large"
                    onClick={confirmSubToggle}
                />
            </div>
        )
    }

    /**
     * Logic to List and display the categories
     */
    const listingCategories = () => {
        return (
            <div className="mt-4 flex gap-2">
                {product.categories && product.categories.map(cat => (
                    <Tag className="pixel-tag pixel-tag-blue" key={cat} value={cat} severity="info"
                         style={{marginTop: 5, marginRight: 5}}/>
                ))}
            </div>
        )
    }

    /**
     * Product page card
     */
    return (
        <div className="p-4">
            <ConfirmPopup/>
            <Card className="product-card">
                <div style={{display: 'flex', gap: '2rem', flexWrap: 'wrap'}}>
                    {imageBox()}
                    <div style={{flex: 1}}>
                        <h1>{product.name}</h1>

                        <p className="mb-4 text-lg" style={{color: 'white'}}>{product.description}</p>
                        {priceTag()}
                        <div style={{display: 'flex', flexDirection: 'column', gap: '1rem', marginTop: '1rem'}}>
                            {amountButtons()}
                            {stockInfo()}
                            {actionButtons()}
                        </div>
                        {listingCategories()}
                    </div>
                </div>
            </Card>
        </div>
    );
};

export default ProductPageComponent;