import { createContext, useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { setupInterceptors } from "../services/apiAuthentication";
import { useLocalStorageState } from "../hook/useLocalStorageState";
import { jwtDecode } from "jwt-decode";
import { useLogout } from "../features/authentication/useLogout";

const AuthContext = createContext()
function AuthContextProvider({ children }) {
    const [token, setToken] = useLocalStorageState(null, "accessToken");
    const [refreshToken, setRefreshToken] = useLocalStorageState(null, "refreshToken");
    const [user, setUser] = useLocalStorageState({}, 'user')

    useEffect(() => {
        setupInterceptors({ setToken, handleLogout });
    }, []);
    useEffect(() => {
        if (token) {
            setToken(token);
        } else {
            setToken(null);
        }
        if (refreshToken) {
            setRefreshToken(refreshToken);
        } else {
            setRefreshToken(null)
        }
    }, [token, refreshToken, setToken, setRefreshToken]);

    const handleLogin = (newToken, newRefreshToken, newUser) => {
        setToken((token) => newToken);
        setRefreshToken((refreshToken) => newRefreshToken);
        setUser(user => newUser)
    };
    const isAuthenticated = () => {
        if (!token) return false;
        try {

            const decoded = jwtDecode(token);
            return decoded.exp * 1000 > Date.now(); // Check if token is still valid
        } catch (error) {
            return false;
        }
    };
    const handleLogout = () => {
        setToken(null);
        setRefreshToken(null)
        setUser(null)
    };

    return (
        <AuthContext.Provider value={{ token, isAuthenticated, handleLogin, user, handleLogout, setToken }}>
            {children}
        </AuthContext.Provider>
    );
}
function useAuthContext() {
    const context = useContext(AuthContext);
    if (context === undefined)
        throw new Error("AuthContext was used outside of AuthContextProvider");
    return context;
}
export { AuthContextProvider, useAuthContext }