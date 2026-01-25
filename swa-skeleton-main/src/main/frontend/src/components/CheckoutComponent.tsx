import React, {useState, useEffect} from 'react';
import {Card} from 'primereact/card';
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {Dropdown} from 'primereact/dropdown';
import {Divider} from 'primereact/divider';
import {useNavigate} from 'react-router-dom';
import {getCart} from '../utilities/cartUtilities';
import {OrderApi} from '../utilities/orderApi';
import {ROUTES} from '../utilities/routes.paths';
import {CartDTO} from '../DTO/cart.types';
import {CheckoutRequestDTO} from '../DTO/checkout.types';
import styles from "./PixelButton.module.css";
import '../styles/Login.css';

const CheckoutComponent: React.FC = () => {
    const navigate = useNavigate();
    const [cart, setCart] = useState<CartDTO>({items: []});

    // Form State
    const [name, setName] = useState('');
    const [street, setStreet] = useState('');
    const [city, setCity] = useState('');
    const [postalCode, setPostalCode] = useState('');
    const [country, setCountry] = useState('');
    const [paymentMethod, setPaymentMethod] = useState('');
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        const currentCart = getCart();
        setCart(currentCart);
        if (currentCart.items.length === 0) {
            navigate(ROUTES.CART);
        }
    }, [navigate]);

    const paymentOptions = [
        {label: 'Credit Card (Visa/Mastercard)', value: 'CREDIT_CARD'},
        {label: 'PayPal', value: 'PAYPAL'},
        {label: 'DuckCoins', value: 'DUCK_COINS'},
        {label: 'Invoice', value: 'INVOICE'}
    ];

    const totalPrice = cart.items.reduce((sum, item) => sum + item.amount * item.pricePerUnit, 0).toFixed(2);

    const handlePlaceOrder = async () => {
        if (!name || !street || !city || !postalCode || !country || !paymentMethod) {
            alert("Please fill in all shipping and payment fields!");
            return;
        }

        setLoading(true);
        const checkoutData: CheckoutRequestDTO = {
            cart: cart,
            shippingName: name,
            shippingStreet: street,
            shippingCity: city,
            shippingPostalCode: postalCode,
            shippingCountry: country,
            paymentMethod: paymentMethod
        };

        try {
            await OrderApi.createOrder(checkoutData);
            localStorage.removeItem('cart');
            alert("Order placed successfully! Thank you for shopping.");
            navigate(ROUTES.ORDERS);
        } catch (error: any) {
            console.error(error);
            const msg = error.response?.data || error.message || "Unknown error";
            alert("Failed to place order: " + msg);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="flex justify-content-center p-4">
            <div className="flex flex-column md:flex-row gap-4 w-full" style={{maxWidth: '1200px'}}>

                {/* Shipping & Payment Form */}
                <Card title="CHECKOUT DETAILS" className="flex-1 product-card">
                    <div className="flex flex-column gap-3">

                        <h3>SHIPPING ADDRESS</h3>
                        <div className="flex flex-column gap-2">
                            <label htmlFor="name" className="font-bold">FULL NAME</label>
                            <InputText id="name" value={name} onChange={(e) => setName(e.target.value)}
                                       className="input-field" placeholder="Duck McQuak"/>
                        </div>

                        <div className="flex flex-column gap-2">
                            <label htmlFor="street" className="font-bold">STREET ADDRESS</label>
                            <InputText id="street" value={street} onChange={(e) => setStreet(e.target.value)}
                                       className="input-field" placeholder="Quakstreet 404"/>
                        </div>

                        <div className="flex gap-3">
                            <div className="flex-1 flex flex-column gap-2">
                                <label htmlFor="postalCode" className="font-bold">POSTAL CODE</label>
                                <InputText id="postalCode" value={postalCode}
                                           onChange={(e) => setPostalCode(e.target.value)} className="input-field"
                                           placeholder="6020"/>
                            </div>
                            <div className="flex-1 flex flex-column gap-2">
                                <label htmlFor="city" className="font-bold">CITY</label>
                                <InputText id="city" value={city} onChange={(e) => setCity(e.target.value)}
                                           className="input-field" placeholder="Ducksbruck"/>
                            </div>
                        </div>

                        <div className="flex flex-column gap-2">
                            <label htmlFor="country" className="font-bold">COUNTRY</label>
                            <InputText id="country" value={country} onChange={(e) => setCountry(e.target.value)}
                                       className="input-field" placeholder="Duckland"/>
                        </div>

                        <Divider className="pixel-divider-dashed"/>

                        <h3>PAYMENT</h3>
                        <div className="flex flex-column gap-2">
                            <label className="font-bold">PAYMENT METHOD</label>
                            <Dropdown
                                value={paymentMethod}
                                options={paymentOptions}
                                onChange={(e) => setPaymentMethod(e.value)}
                                placeholder="SELECT A PAYMENT METHOD"
                                className="pixel-dropdown w-full"
                            />
                        </div>
                    </div>
                </Card>

                {/* Order Summary */}
                <Card title="ORDER SUMMARY" className="flex-initial md:w-30rem product-card h-fit">
                    <div className="flex flex-column">
                        <ul style={{listStyle: 'none', padding: 0, margin: 0}}>
                            {cart.items.map(item => (
                                <li key={item.productId}
                                    className="flex justify-content-between mb-3 border-bottom-1 surface-border pb-2">
                                    <div className="flex flex-column">
                                        <span className="font-bold">{item.productName}</span>
                                        <span className="text-sm">Qty: {item.amount}</span>
                                    </div>
                                    <span className="font-bold">{(item.amount * item.pricePerUnit).toFixed(2)} €</span>
                                </li>
                            ))}
                        </ul>

                        <div className="flex justify-content-between text-xl font-bold mt-4 mb-4">
                            <span>TOTAL</span>
                            <span>{totalPrice} €</span>
                        </div>

                        <Button
                            label={loading ? "PROCESSING..." : "COMPLETE ORDER"}
                            icon="pi pi-check"
                            className={`${styles.btn} ${styles.btn_green} w-full`}
                            onClick={handlePlaceOrder}
                            disabled={loading}
                        />
                        <Button
                            label="BACK TO CART"
                            className="p-button-text pixel-link w-full mt-2"
                            style={{justifyContent: 'center'}}
                            onClick={() => navigate(ROUTES.CART)}
                        />
                    </div>
                </Card>
            </div>
        </div>
    );
};

export default CheckoutComponent;