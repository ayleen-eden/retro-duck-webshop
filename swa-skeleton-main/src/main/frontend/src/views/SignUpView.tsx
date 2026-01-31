import React from 'react';
import NavbarComponent from '../components/NavbarComponent';
import SignUpComponent from '../components/SignUpComponent';

/**
 * View component for user registration (Sign Up).
 * * This view provides the layout for the registration process. It wraps the
 * {@link SignUpComponent} and ensures that the {@link NavbarComponent} is
 * present at the top of the page.
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