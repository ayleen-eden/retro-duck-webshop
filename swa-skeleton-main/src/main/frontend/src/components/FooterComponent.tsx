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
            {/* Left: Credits */}
            <div style={{display: 'flex', flexDirection: 'column', lineHeight: '1.2'}}>
                <span style={{fontWeight: 'bold', fontSize: '0.9rem'}}>
                    Software Architecture Project (WS 2025/26) &bull; University of Innsbruck
                </span>
                <span style={{fontSize: '0.8rem', color: 'gray'}}>
                    Devs: Ayleen Edenhauser, Matteo Volperino, Jakob Oberhofer, Leonid Stommel
                </span>
            </div>

            {/* Right: User Info */}
            <div style={{display: 'flex', flexDirection: 'column', alignItems: 'flex-end', lineHeight: '1.2'}}>
                <span style={{fontSize: '0.9rem'}}>
                    Logged in as: <strong>{currentUser?.firstName} {currentUser?.lastName}</strong> ({currentUser?.username || 'Guest'})
                </span>
                <span style={{display: 'flex', alignItems: 'center', fontSize: '0.9rem', marginTop: '4px'}}>
                    Roles:&ensp; {currentUser ? rolesBodyTemplate(currentUser) : "Guest"}
                </span>
            </div>
        </footer>
    );
};