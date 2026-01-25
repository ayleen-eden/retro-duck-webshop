import React from 'react';
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import CheckoutComponent from "../components/CheckoutComponent";

const CheckoutView: React.FC = () => {
    return (
        <div style={{display: 'flex', flexDirection: 'column', minHeight: '100vh'}}>
            <NavbarComponent/>
            <div className="main-content" style={{flex: 1, paddingBottom: '7rem'}}>
                <CheckoutComponent/>
            </div>
            <FooterComponent/>
        </div>
    );
};

export default CheckoutView;