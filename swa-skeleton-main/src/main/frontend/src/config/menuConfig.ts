import {UserxRole} from "../DTO/userx.types";
import {ROUTES} from "../utilities/routes.paths";

export type MenuItemConfig = {
    label: string;
    icon?: string;
    route?: string;
    roles?: UserxRole[];
    items?: MenuItemConfig[];
};

export const menuConfig: MenuItemConfig[] = [
    {
        label: 'Home', icon: 'pi pi-home', route: ROUTES.HOME
    }, {
        label: 'Admin Submenu', icon: 'pi pi-shield',
        roles: [UserxRole.ADMIN],
        items: [{
            label: 'Manage Users', icon: 'pi pi-users', route: ROUTES.MANAGE_USERS, roles: [UserxRole.ADMIN]
        }]
    }, {
        label: "Logout", icon: "pi pi-sign-out", route: ROUTES.LOGOUT, roles: [UserxRole.CUSTOMER]
    }, {
        label: "Shopping cart", icon: "pi pi-shopping-cart", route: ROUTES.CART
    }, {
        label: "Single Product Page (DEMO)", icon: "pi pi-barcode", route: ROUTES.PRODUCT_PAGE
    }, {
        label: 'My Orders', icon: 'pi pi-list', route: ROUTES.ORDERS
    }
];