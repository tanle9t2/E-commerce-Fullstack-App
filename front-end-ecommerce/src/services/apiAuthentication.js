import axios from "axios";
import { useContext } from "react";
import { useAuthContext } from "../context/AuthContext";

const AUTHENTICATION_API = "http://localhost:8080/ecommerce-server/api/v1"
const api = axios.create({
    baseURL: AUTHENTICATION_API,
    headers: { "Content-Type": "application/json" },
    withCredentials:true
});
export const setupInterceptors = ({ setToken, handleLogout }) => {
    api.interceptors.response.use(
        (response) => response, // Return successful response
        async (error) => {
            const originalRequest = error.config;

            // If 401 Unauthorized and token has not been retried yet
            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                try {
                    const refreshToken = localStorage.getItem("refreshToken");
                    const response = await axios.post(
                        "/user/refresh-token",
                        { refreshToken }
                    );

                    // Update token
                    const newAccessToken = response.data.accessToken;
                    setToken(newAccessToken);  // Update React state
                    setAuthToken(newAccessToken); // Update axios header

                    // Retry the original request with new token
                    originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
                    return axios(originalRequest);
                } catch (refreshError) {
                    handleLogout(); // Log user out if refresh fails
                    return Promise.reject(refreshError);
                }
            }
            return Promise.reject(error);
        }
    );
};
// Set Authorization header dynamically
export const setAuthToken = (token) => {
    if (token) {
        api.defaults.headers.common["Authorization"] = `Bearer ${token}`;
    } else {
        delete api.defaults.headers.common["Authorization"];
    }
};

export async function loginUser({username,password}) {
    const res = await fetch(`${AUTHENTICATION_API}/user/login`, {
        method: "POST",
        headers: {
            "Content-Type": "application/json",
        },
        body: JSON.stringify({username,password}),
    });
    if(res.status === 401) 
        throw new Error("Username/Password invalid")
    const result = await res.json();
    return result;
    
}
export async function getUser() {
    const token = localStorage.getItem("accessToken");

    if (!token) {
        console.error("No token found!");
        throw new Error("Authentication token is missing");
    }
    try {
        const response = await api.get("/user/", {
            headers: { Authorization: `Bearer ${token}` },
        });
        return response.data;
    } catch (error) {
        console.error("Error fetching user data", error.response || error);
        throw error; 
    }
}
