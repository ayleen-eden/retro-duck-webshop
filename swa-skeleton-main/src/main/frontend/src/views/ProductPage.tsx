import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React from "react";
import NavbarComponent from "../components/NavbarComponent";
import ProductPageComponent from "../components/ProductPageComponent";
import {FooterComponent} from "../components/FooterComponent";
import {useParams} from "react-router-dom";
import RatingComponent from "../components/RatingComponent";

const ProductPage: React.FC =() => {
    const {productId} = useParams<{ productId: string }>();


        return (
            <div>
                <NavbarComponent/>
                <ProductPageComponent productId={Number(productId)}/>
                <RatingComponent productId={Number(productId)}/>
                <FooterComponent/>
            </div>
        );
    }

export default ProductPage;