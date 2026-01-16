import React, {useEffect, useRef, useState} from 'react';
import NavbarComponent from "../components/NavbarComponent";
import {FooterComponent} from "../components/FooterComponent";
import {InputText} from 'primereact/inputtext';
import {Button} from 'primereact/button';
import {Password} from 'primereact/password';
import {Toast} from 'primereact/toast';
import {UserxApi, UserProfileUpdateDTO} from "../utilities/userxApi";
import {UserxTypes} from "../DTO/userx.types";
import styles from "../components/PixelButton.module.css";
import '../styles/Login.css';

const UserProfile: React.FC = () => {
    const [firstName, setFirstName] = useState('');
    const [lastName, setLastName] = useState('');
    const [email, setEmail] = useState('');
    const [phone, setPhone] = useState('');
    const [username, setUsername] = useState('');
    const [newPassword, setNewPassword] = useState('');
    const [confirmPassword, setConfirmPassword] = useState('');

    const [loading, setLoading] = useState(true);
    const toast = useRef<Toast>(null);

    useEffect(() => {
        loadUserData();
    }, []);

    const loadUserData = async () => {
        try {
            const user: UserxTypes = await UserxApi.getCurrentUser();
            setFirstName(user.firstName);
            setLastName(user.lastName);
            setEmail(user.email);
            setPhone(user.phone);
            setUsername(user.username);
        } catch (e) {
            console.error("Failed to load profile", e);
        } finally {
            setLoading(false);
        }
    };

    const handleSave = async (e: React.FormEvent) => {
        e.preventDefault();

        if (newPassword && newPassword !== confirmPassword) {
            toast.current?.show({severity: 'error', summary: 'Error', detail: 'Passwords do not match!'});
            return;
        }

        const updateData: UserProfileUpdateDTO = {
            firstName,
            lastName,
            email,
            phone,
            password: newPassword ? newPassword : undefined
        };

        try {
            await UserxApi.updateCurrentUserProfile(updateData);
            toast.current?.show({severity: 'success', summary: 'Success', detail: 'Profile updated successfully!'});
            setNewPassword('');
            setConfirmPassword('');
        } catch (err) {
            console.error(err);
            toast.current?.show({severity: 'error', summary: 'Error', detail: 'Could not update profile.'});
        }
    };

    if (loading) return <div className="login-container">Loading...</div>;

    return (
        <div>
            <NavbarComponent/>
            <Toast ref={toast}/>

            <div className="login-container" style={{height: 'auto', minHeight: '90vh', padding: '100px 0'}}>
                <div className="login-card" style={{width: '600px'}}>
                    <h2 style={{textAlign: 'center', marginBottom: '2rem'}}>Edit Profile</h2>

                    <form onSubmit={handleSave} className="flex flex-column gap-3">

                        {/* Username (Read Only) */}
                        <div className="flex flex-column gap-2 mb-3">
                            <label htmlFor="username">Username (Read-only)</label>
                            <InputText
                                id="username"
                                value={username}
                                disabled
                                className="input-field"
                                style={{opacity: 0.7}}
                            />
                        </div>

                        {/* Name Fields */}
                        <div className="flex gap-3">
                            <div className="flex flex-column gap-2 w-full">
                                <label htmlFor="firstName">First Name</label>
                                <InputText
                                    id="firstName"
                                    value={firstName}
                                    onChange={(e) => setFirstName(e.target.value)}
                                    className="input-field"
                                />
                            </div>
                            <div className="flex flex-column gap-2 w-full">
                                <label htmlFor="lastName">Last Name</label>
                                <InputText
                                    id="lastName"
                                    value={lastName}
                                    onChange={(e) => setLastName(e.target.value)}
                                    className="input-field"
                                />
                            </div>
                        </div>

                        {/* Contact Info */}
                        <div className="flex flex-column gap-2 mb-3">
                            <label htmlFor="email">E-Mail</label>
                            <InputText
                                id="email"
                                value={email}
                                onChange={(e) => setEmail(e.target.value)}
                                className="input-field"
                            />
                        </div>

                        <div className="flex flex-column gap-2 mb-3">
                            <label htmlFor="phone">Phone</label>
                            <InputText
                                id="phone"
                                value={phone}
                                onChange={(e) => setPhone(e.target.value)}
                                className="input-field"
                            />
                        </div>

                        <div className="pixel-divider-dashed"></div>

                        {/* Password Section */}
                        <h3>Change Password</h3>
                        <p style={{fontSize: '0.7rem', color: '#aaa', marginBottom: '1rem'}}>
                            Leave blank if you don't want to change it.
                        </p>

                        <div className="flex gap-3">
                            <div className="flex flex-column gap-2 w-full">
                                <label htmlFor="newPass">New Password</label>
                                <Password
                                    inputId="newPass"
                                    value={newPassword}
                                    onChange={(e) => setNewPassword(e.target.value)}
                                    toggleMask
                                    feedback={false}
                                    className="input-field"
                                    inputClassName="input-field"
                                    pt={{
                                        input: {style: {width: '100%'}}
                                    }}
                                />
                            </div>
                            <div className="flex flex-column gap-2 w-full">
                                <label htmlFor="confPass">Confirm</label>
                                <Password
                                    inputId="confPass"
                                    value={confirmPassword}
                                    onChange={(e) => setConfirmPassword(e.target.value)}
                                    toggleMask
                                    feedback={false}
                                    className="input-field"
                                    inputClassName="input-field"
                                    pt={{
                                        input: {style: {width: '100%'}}
                                    }}
                                />
                            </div>
                        </div>

                        <div className="flex justify-content-end mt-4">
                            <Button
                                label="Save Changes"
                                icon="pi pi-save"
                                className={`${styles.btn} ${styles.btn_green}`}
                                type="submit"
                            />
                        </div>
                    </form>
                </div>
            </div>

            <FooterComponent/>
        </div>
    );
};

export default UserProfile;