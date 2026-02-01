import React from "react";
import {Card} from "primereact/card";
import logo from "../logo.svg";

/**
 * Contact page component.
 *
 * @returns TSX element representing the contact page
 */
export const ContactComponent: React.FC = () => {
    return (
        <Card title="CONTACT" className="product-card">
        <div>
            <div style={{display: 'flex', flexDirection: 'column', lineHeight: '1.2', justifyContent:"center", alignItems:"center"}}>
                <img src={logo} className="App-logo" alt="logo" style={{height: '150px'}}/>
                <span style={{fontWeight: 'bold', fontSize: '1.8rem', marginBottom: '1rem'}}>
                    <h2>THE RETRO DUCK </h2>
                </span>
                <span style={{fontWeight: 'bold', fontSize: '1.8rem', marginBottom: '1rem'}}>
                    <h3> Software Architecture Project (WS 2025/26) </h3>
                </span>
                <span style={{fontWeight: 'bold', fontSize: '1.8rem', marginBottom: '1rem'}}>
                    <h3> University of Innsbruck </h3>
                </span>
                <span style={{fontSize: '1.5rem', color: 'gray', justifyContent:"center", alignItems:"center", marginBottom: '1rem'}}>
                    FIND OUR DEVS ON GITLAB!
                </span>
                <span style={{display: 'flex', flexDirection: 'column', fontSize: '1.5rem', justifyContent:"center", alignItems:"center", marginBottom: '1rem'}}>
                    <a href="https://git.uibk.ac.at/csbb8882" className="link link_red">Ayleen Edenhauser</a>
                    <a href="https://git.uibk.ac.at/csbb8047" className="link link_red">Matteo Volperino</a>
                    <a href="https://git.uibk.ac.at/csbb9206" className="link link_red">Jakob Oberhofer</a>
                    <a href="https://git.uibk.ac.at/csbc4400" className="link link_red">Leonid Stommel</a>
                </span>
                <span style={{fontSize: '1.5rem', color: 'gray', justifyContent:"center", alignItems:"center", marginBottom: '1rem'}}>
                    ANYTHING YOU DON'T LIKE ABOUT THE RETRO DUCK? WE DON'T CARE!
                </span>
            </div>
        </div>
        </Card>
    )
}