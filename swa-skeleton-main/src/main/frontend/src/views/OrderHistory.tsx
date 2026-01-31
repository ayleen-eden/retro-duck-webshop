import React from 'react';
import NavbarComponent from "../components/NavbarComponent";
import { FooterComponent } from "../components/FooterComponent";
import OrderHistoryComponent from "../components/OrderHistoryComponent";
import '../styles/App.css';

/**
 * View component for the Order History page.
 * * This page serves as a container for the {@link OrderHistoryComponent}.
 */
const OrderHistoryView: React.FC = () => {
    return (
        <div className="scroll-container">
            <NavbarComponent />
            <OrderHistoryComponent />
            <FooterComponent />
        </div>
    );
};

export default OrderHistoryView;