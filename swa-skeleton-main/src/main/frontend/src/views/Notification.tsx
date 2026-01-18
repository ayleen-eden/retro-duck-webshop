import '../styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React from "react";
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import {NotificationComponent} from "../components/NotificationComponent";
import {SubscriptionComponent} from "../components/SubscriptionComponent";
import { Divider } from 'primereact/divider';

class Notification extends React.Component {
    render() {
        return (
            <div scroll-container>
                <NavbarComponent/>
                <NotificationComponent/>
                <Divider className="pixel-divider-dashed"/>
                <SubscriptionComponent/>
                <FooterComponent/>
            </div>
        );
    }
}

export default Notification;
