import React from "react";
import {useUser} from "../Contexts/authenticatedUserContext";
import "../styles/Footer.css"
import {rolesBodyTemplate} from "./rolesBodyTemplate";


export const FooterComponent: React.FC = () => {

    // get user context
    const {currentUser} = useUser();

    return (
        <footer style={{
            display: 'flex',
            justifyContent: 'space-between',
            alignItems: 'center',
            padding: '0.5rem 2rem',
            height: 'auto',
            minHeight: '60px',
            flexWrap: 'wrap'
        }}>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-start', lineHeight: '1.2'}}>
                <span style={{fontSize: '0.9rem'}}>
                    LOGGED IN AS: <strong>{currentUser?.firstName} {currentUser?.lastName}</strong> ({currentUser?.username || 'Guest'})
                </span>
                <span style={{display: 'flex', alignItems: 'center', fontSize: '0.9rem', marginTop: '4px'}}>
                    ROLES:&ensp; {currentUser ? rolesBodyTemplate(currentUser) : "Guest"}
                </span>
            </div>
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end', lineHeight: '1.2'}}>
                <span style={{display: 'flex', alignItems: 'center', fontSize: '0.9rem', marginTop: '4px'}}>
                <a href="/contact" className="link link_red">WHO ARE WE?</a>
                </span>
            </div>
        </footer>
    );
};