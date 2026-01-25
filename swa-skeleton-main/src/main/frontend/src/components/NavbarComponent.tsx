/**
 * This code is part of the skeleton project provided for students of the course "Software
 * Architecture" offered by Innsbruck University.
 */
import React from 'react';
import {Menubar} from "primereact/menubar";

import {useUser} from "../Contexts/authenticatedUserContext";
import {menuConfig, MenuItemConfig} from "../config/menuConfig";
import {MenuItem} from "primereact/menuitem";
import {Link, useNavigate} from "react-router-dom";
import {ROUTES} from "../utilities/routes.paths";
import {Button} from 'primereact/button';

/**
 * Navbar component.
 */
const NavbarComponent: React.FC = () => {
    const {currentUser: user} = useUser();

    const filterMenu = React.useCallback((items: MenuItemConfig[]): MenuItemConfig[] => {

        return items
            .map(item => {
                const visibleChildren = item.items ? filterMenu(item.items) : undefined;
                return {...item, items: visibleChildren};
            })
            .filter(item => {
                if (!item.roles || item.roles.length === 0) {
                    return true;
                }

                if (!user) {
                    return false;
                }
                const hasRole = item.roles.some(r => user.roles.includes(r));
                const hasVisibleChildren = !!item.items?.length;

                return hasRole || hasVisibleChildren;
            });
    }, [user]);

    // we want to use navigate (react router) to ensure pure client-side navigation on menu item click
    // incidentally, we also want to fix primereact component-related aria warnings
    const buildMenubar = React.useCallback((items: MenuItemConfig[]): MenuItem[] => {
        return items.map(configItem => {
            const children = configItem.items ? buildMenubar(configItem.items) : undefined;

            const menuItem: MenuItem = {
                label: configItem.label,
                icon: configItem.icon,
                items: children,
            };

            menuItem.template = (menuItem, options) => {
                const handleClick = (e: React.MouseEvent<HTMLElement>) => {
                    options.onClick?.(e);
                };

                return (
                    <Link
                        to={configItem.route ?? "#"}
                        className="pixel-link"
                        onClick={handleClick}
                    >
                        {menuItem.icon && <span className={options.iconClassName}/>}
                        <span className={options.labelClassName}>{menuItem.label}</span>
                    </Link>
                );
            }
            return menuItem;
        });
    }, []);

    const filteredItems = React.useMemo(() => filterMenu(menuConfig), [filterMenu]);
    const model = React.useMemo(() => buildMenubar(filteredItems), [filteredItems, buildMenubar]);

    const navigate = useNavigate();

    const end = (
        <Button
            label={user ? "LOGOUT" : "LOGIN"}
            icon={user ? "pi pi-sign-out" : "pi pi-sign-in"}
            className="p-button-text pixel-link"
            onClick={() => {
                if (user) {
                    navigate(ROUTES.LOGOUT);
                } else {
                    navigate(ROUTES.LOGIN);
                }
            }}
        />
    );

    return (
        <div className="sticky-navbar">
            <Menubar model={model} end={end}/>
        </div>
    );
}

export default NavbarComponent;