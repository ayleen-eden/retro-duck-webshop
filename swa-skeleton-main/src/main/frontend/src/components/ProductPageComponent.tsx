import React, {useState} from "react";
import { Divider } from 'primereact/divider';
import {FilterService} from "primereact/api";
import {useParams} from "react-router-dom";
import NavbarComponent from "./NavbarComponent";
import {dummyProduct1} from "./DebugProducts";
import {ProductDTO} from "../DTO/product.types";
import {Card} from "primereact/card";
import {Button} from "primereact/button";
import {InputNumber, InputNumberValueChangeEvent} from "primereact/inputnumber";
import {useSessionStorage} from "primereact/hooks";
import {Galleria} from "primereact/galleria";
import {Tag} from "primereact/tag";
import {Rating} from "primereact/rating";
import RatingComponent from "./RatingComponent";

FilterService.register('custom_range', (value, filters) => {
    const [from, to] = filters ?? [null, null];
    if (from === null && to === null) return true;
    if (from !== null && to === null) return from <= value;
    if (from === null && to !== null) return value <= to;
    return from <= value && value <= to;
});

const ProductPageComponent: React.FC = () => {
    const {productId} = useParams<{productId: string}>();
    const USE_DUMMY = true;
    const [product, setProduct] = useState<ProductDTO>(
        dummyProduct1
    );
    const [quantity, setQuantity] = useSessionStorage<number>(1, 'quantity');

    return (
        <div>
        <Card>
            <div
                style={{
                    display: 'flex',
                    justifyContent: 'space-between',
                    alignItems: 'flex-start',
                }}
            >
                <div>
                    <img
                        src={`/images/kiryu_duck.png`}
                        alt="Kiryu Duck"
                        style={{ width: 500, height: 500 }}
                    />
                </div>


                <div style={{ flex: 1 }}>
                    <h1>{product.name}</h1>
                    <Rating value={5} readOnly cancel={false}/>
                    <p>
                        NOT FOR SALE! Debug Duck used to test how the product page looks. Not to be confused with the equally iconic shopping cart duck!
                    </p>
                    <span style={{color: "red"}}>
                    <h3>Discounted price: 26.99 € per unit</h3>
                    </span>
                    <span style={{ textDecoration: 'line-through' }}>
                        <h4>Original price: {product.price} €</h4>
                    </span>
                    <div style={{
                        display: 'flex',
                        gap: '3rem',
                        alignItems: 'flex-start'
                    }}>
                            <div style={{
                                display: 'flex',
                                gap: '1rem'
                            }}>
                                <Button icon="pi pi-minus" className="p-button-rounded p-button-outlined p-button-warning" size="small" onClick={() => setQuantity(Math.max(1,quantity - 1))}></Button>
                                <span className="font-bold text-4xl mb-5" style={{ marginTop: '0.85rem' }}>{quantity}</span>
                                <Button icon="pi pi-plus" className="p-button-rounded p-button-outlined p-button-success" size="small" onClick={() => setQuantity(quantity + 1)}></Button>
                            </div>
                        <Button  label = "Add to cart" icon="pi pi-cart-plus"/>
                    </div>
                    <Tag value="Duck" className="p-button-warning" style={{marginTop: '0.85rem'}}/>
                    <div style={{
                        display: 'flex',
                        gap: '3rem'
                    }}>
                        <Button icon="pi pi-bell" label = "Get Notified" className="p-button-warning" size="small" style={{marginTop: '0.85rem'}}/>
                    </div>
                </div>
            </div>
        </Card>
        <RatingComponent productId={Number(productId)} />
        </div>
    );
};

export default ProductPageComponent