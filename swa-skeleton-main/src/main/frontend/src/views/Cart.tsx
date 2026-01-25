import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React from "react";
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import CartComponent from "../components/CartComponent";

class Cart extends React.Component {
    render() {
        return (
            <div scroll-container>
                <NavbarComponent/>
                <CartComponent/>
                <FooterComponent/>
            </div>
        );
    }
}

export default Cart;
