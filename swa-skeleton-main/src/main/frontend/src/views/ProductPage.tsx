import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React from "react";
import NavbarComponent from "../components/NavbarComponent";
import ProductPageComponent from "../components/ProductPageComponent";
import {FooterComponent} from "../components/FooterComponent";

class ProductPage extends React.Component {
    render() {
        return (
            <div>
                <NavbarComponent/>
                <ProductPageComponent/>
                <FooterComponent/>
            </div>
        );
    }
}

export default ProductPage;