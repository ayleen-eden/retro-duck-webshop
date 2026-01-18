import React from 'react';
import NavbarComponent from '../components/NavbarComponent';
import SignUpComponent from '../components/SignUpComponent';

/**
 * View for the user registration (Sign Up).
 */
const SignUpView: React.FC = () => {
    return (
        <>
            <NavbarComponent />
            <div className="view-container">
                <SignUpComponent />
            </div>
        </>
    );
};

export default SignUpView;