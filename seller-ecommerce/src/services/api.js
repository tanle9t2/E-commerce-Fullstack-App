import axios from "axios";
import { getAuth, setAuth } from "../utils/helper";
import { useAuthContext } from "../context/AuthContext";

const BASE_URL = "http://localhost:8080/ecommerce-server/api/v1";

export const api = axios.create({
    baseURL: BASE_URL,
    headers: { "Content-Type": "application/json" },
});

export default api;
export function setAccessToken(token) {
    if (token) {
        axiosPrivate.defaults.headers["Authorization"] = `Bearer ${token}`;
    } else {
        delete axiosPrivate.defaults.headers["Authorization"];
    }
}
export const axiosPrivate = axios.create({
    baseURL: BASE_URL,
    headers: { "Content-Type": "application/json" },
    withCredentials: true,
});

// Store refresh promise to prevent multiple refresh calls
let refreshTokenPromise = null;

axiosPrivate.interceptors.request.use(
    (config) => {
        const { accessToken } = getAuth();
        if (accessToken) {
            config.headers["Authorization"] = `Bearer ${accessToken}`;
        }
        return config;
    },
    (error) => Promise.reject(error)
);

axiosPrivate.interceptors.response.use(
    (response) => response,
    async (error) => {
        const originalRequest = error.config;

        // If token is expired and request is not retried yet
        if (error.response?.status === 401 && !originalRequest._retry) {
            originalRequest._retry = true;

            // If a refresh request is already in progress, wait for it
            if (!refreshTokenPromise) {

                refreshTokenPromise = refreshAccessToken();
            }

            try {
                const newAccessToken = await refreshTokenPromise;
                refreshTokenPromise = null; // Reset the promise after success

                // Set the new token for future requests
                axiosPrivate.defaults.headers["Authorization"] = `Bearer ${newAccessToken}`;
                originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;

                return axiosPrivate(originalRequest); // Retry failed request
            } catch (refreshError) {
                const { handleLogout } = useAuthContext()
                refreshTokenPromise = null;
                console.error("Token refresh failed:", refreshError);
                setAuth({})
                handleLogout()
                return Promise.reject(refreshError);
            }
        }
        return Promise.reject(error);
    }
);

// 🔹 Function to Refresh Token
async function refreshAccessToken() {
    const { refreshToken } = getAuth();
    if (!refreshToken) throw new Error("No refresh token available");

    const refreshResponse = await api.post("/user/refreshToken", {}, {
        headers: { Authorization: `Bearer ${refreshToken}` },
    });

    const { accessToken: newAccessToken } = refreshResponse.data;
    setAuth({ accessToken: newAccessToken, refreshToken });

    return newAccessToken;
}
