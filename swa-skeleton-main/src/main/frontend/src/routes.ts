/**
 * This code is part of the skeleton project provided for students of the course "Software
 * Architecture" offered by Innsbruck University.
 */

import HomePage from "./views/HomePage";
import ManageUsers from "./views/ManageUsers";
import Login from "./views/Login";
import Logout from "./views/Logout";
import Cart from "./views/Cart";
import {ROUTES} from "./utilities/routes.paths";
import ProductPage from "./views/ProductPage";
import OrderHistory from "./views/OrderHistory";

/**
 * Define the routes of the application.
 */

export const HomePageRoute = {
    url: ROUTES.HOME,
    component: HomePage
}

export const ManageUsersRoute = {
    url: ROUTES.MANAGE_USERS,
    component: ManageUsers
}
export const LoginsRoute = {
    url: ROUTES.LOGIN,
    component: Login
}
export const LogoutsRoute = {
    url: ROUTES.LOGOUT,
    component: Logout
}

export const CartRoute = {
    url: ROUTES.CART,
    component: Cart
}

export const ProductPageRoute = {
    url: ROUTES.PRODUCT_PAGE,
    component: ProductPage
}

export const OrderHistoryRoute = {
    url: ROUTES.ORDERS,
    component: OrderHistory
}
