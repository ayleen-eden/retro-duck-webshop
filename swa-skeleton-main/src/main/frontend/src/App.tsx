/**
 * This code is part of the skeleton project provided for students of the course "Software
 * Architecture" offered by Innsbruck University.
 */
import './styles/App.css';
import "primereact/resources/themes/lara-light-cyan/theme.css";
import React, {Suspense} from "react";
import {BrowserRouter, Route, Routes} from "react-router-dom";
import {
    CartRoute,
    HomePageRoute,
    LoginsRoute,
    LogoutsRoute,
    ManageUsersRoute,
    ProductPageRoute,
} from "./routes";
import PrivateRoute from './components/PrivateRoute';
import {UserProvider} from "./Contexts/authenticatedUserContext";
import ProductPageComponent from "./components/ProductPageComponent";

const App: React.FC = () => {
    return (
        // Wrap the application in the UserProvider, which allows to access the authenticated user
        <UserProvider>
            <Suspense fallback={<div>Loading...</div>}>
                <BrowserRouter>
                    <Routes>
                        <Route path={LoginsRoute.url} Component={LoginsRoute.component}/>
                        <Route path={CartRoute.url} Component={CartRoute.component}/>
                        <Route path={HomePageRoute.url} Component={HomePageRoute.component}/>
                        <Route path={ProductPageRoute.url} element={<ProductPageRoute.component/>}/>
                        /* Protected Routes (authentication required) */
                        <Route element={<PrivateRoute/>}>
                            <Route path={ManageUsersRoute.url} Component={ManageUsersRoute.component}/>
                            <Route path={LogoutsRoute.url} Component={LogoutsRoute.component}/>
                        </Route>
                        {/* end of protected routes */}
                    </Routes>
                </BrowserRouter>
            </Suspense>
        </UserProvider>
    );
}

export default App;
