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
        label: "My Profile", icon: "pi pi-user", route: ROUTES.PROFILE
    }, {
        label: 'My Orders', icon: 'pi pi-list', route: ROUTES.ORDERS
    }, {
        label: "Shopping cart", icon: "pi pi-shopping-cart", route: ROUTES.CART
    }, {
        label: 'Notifications', icon: 'pi pi-bell', route: ROUTES.NOTIFICATION
    }, {
        label: "Manage Products", icon: "pi pi-box", route: ROUTES.MANAGE_PRODUCTS, roles: [UserxRole.MANAGER, UserxRole.ADMIN]
    }, {
        label: "Logout", icon: "pi pi-sign-out", route: ROUTES.LOGOUT, roles: [UserxRole.CUSTOMER]
    },
];
