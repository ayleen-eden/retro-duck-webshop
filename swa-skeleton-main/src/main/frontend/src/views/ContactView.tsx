import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React from "react";
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import {ContactComponent} from "../components/ContactComponent";

class Cart extends React.Component {
    render() {
        return (
            <div scroll-container>
                <NavbarComponent/>
                <ContactComponent/>
                <FooterComponent/>
            </div>
        );
    }
}

export default Cart;
