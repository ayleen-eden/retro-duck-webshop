import React, {useState} from 'react';
import {Button} from 'primereact/button';
import {InputText} from 'primereact/inputtext';
import {Password} from 'primereact/password';
import {FloatLabel} from 'primereact/floatlabel';
import {useNavigate} from 'react-router-dom';
import {ROUTES} from '../utilities/routes.paths';
import '../styles/Login.css';
import { UserxApi } from '../utilities/userxApi';

/**
 * Component for user registration.
 * * This component manages the registration form state and performs the
 * API call to the backend. It specifically handles conflict responses
 * (HTTP 409) which are triggered by the backend's {@code UsernameDuplicateException}
 * when a chosen username is already in use.
 */
const SignUpComponent: React.FC = () => {
    const [userData, setUserData] = useState({
        username: '',
        email: '',
        firstName: '',
        lastName: '',
        password: '',
        phone: ''
    });
    const [error, setError] = useState<string | null>(null);
    const navigate = useNavigate();

    const handleSignUp = async (e: React.FormEvent) => {
        e.preventDefault();
        setError(null);
        try {
            await UserxApi.registerUser(userData);
            alert("Registration succesful!");
            navigate(ROUTES.LOGIN);
        } catch (err: any) {
            if (err.response) {
                if (err.response.status === 409) {
                    const message = err.response.data.message || err.response.data;
                    setError(message);
                } else {
                    setError("Registration failed (Status " + err.response.status + ")");
                }
            } else {
                setError("Server not reachable. Please check your connection");
            }
        }
    };

    return (
        <div className="login-container">
            <div className="login-card">
                <h2 className="pixel-text">SIGN UP</h2>
                <form onSubmit={handleSignUp}>
                    <FloatLabel style={{marginTop: 30}}>
                        <InputText id="username" value={userData.username} onChange={(e) => setUserData({...userData, username: e.target.value})} required className="input-field" />
                        <label htmlFor="username">USERNAME</label>
                    </FloatLabel>
                    <FloatLabel style={{marginTop: 25}}>
                        <InputText id="firstName" value={userData.firstName} onChange={(e) => setUserData({...userData, firstName: e.target.value})} required className="input-field" />
                        <label htmlFor="firstName">FIRST NAME</label>
                    </FloatLabel>
                    <FloatLabel style={{marginTop: 25}}>
                        <InputText id="lastName" value={userData.lastName} onChange={(e) => setUserData({...userData, lastName: e.target.value})} required className="input-field" />
                        <label htmlFor="lastName">LAST NAME</label>
                    </FloatLabel>
                    <FloatLabel style={{marginTop: 25}}>
                        <InputText id="email" value={userData.email} onChange={(e) => setUserData({...userData, email: e.target.value})} required className="input-field" />
                        <label htmlFor="email">E-MAIL</label>
                    </FloatLabel>
                    <FloatLabel style={{marginTop: 25}}>
                        <InputText id="phone" value={userData.phone} onChange={(e) => setUserData({...userData, phone: e.target.value})} required className="input-field" />
                        <label htmlFor="phone">PHONE NUMBER</label>
                    </FloatLabel>
                    <FloatLabel style={{marginTop: 25}}>
                        <Password inputId="password" value={userData.password} onChange={(e) => setUserData({...userData, password: e.target.value})} required feedback={false} className="input-field" />
                        <label htmlFor="password">PASSWORD</label>
                    </FloatLabel>
                    <div style={{
                        display: 'flex',
                        gap: '15px',
                        marginTop: '40px',
                        alignItems: 'stretch',
                        justifyContent: 'center',
                        width: '100%'
                    }}>
                        <Button
                            type="button"
                            label="BACK TO LOGIN"
                            className="p-button-text pixel-link"
                            onClick={() => navigate(ROUTES.LOGIN)}
                            style={{
                                flex: 1,
                                height: '50px',
                                fontSize: '0.8rem',
                                color: 'white',
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center',
                                backgroundColor: 'transparent',
                            }}
                        />
                        <Button
                            type="submit"
                            label="REGISTER"
                            className="loginButton"
                            style={{
                                flex: 1,
                                height: '50px',
                                margin: 0,
                                display: 'flex',
                                alignItems: 'center',
                                justifyContent: 'center'
                            }}
                        />
                    </div>
                </form>
                {error && <p style={{color: '#ff7675', marginTop: 20, fontSize: '0.8rem'}}>{error}</p>}
            </div>
        </div>
    );
};

export default SignUpComponent;