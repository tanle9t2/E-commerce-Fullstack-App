import { data } from "autoprefixer";
import { getAuthHeaders } from "../utils/helper";
import { createAPI } from "./api";
import axios from "axios";

const AUTHENTICATION_API = "http://localhost:8080/ecommerce-server/api/v1";
const authAPI = createAPI(AUTHENTICATION_API);

// Setup Axios Interceptors for Token Refresh
export const setupInterceptors = ({ setToken, handleLogout }) => {
    authAPI.interceptors.response.use(
        (response) => response,
        async (error) => {
            const originalRequest = error.config;

            if (error.response?.status === 401 && !originalRequest._retry) {
                originalRequest._retry = true;

                try {
                    const refreshToken = localStorage.getItem("refreshToken");
                    const response = await authAPI.post("/user/refresh-token", { refreshToken });

                    // Update token
                    const newAccessToken = response.data.accessToken;
                    setToken(newAccessToken);
                    authAPI.setAuthToken(newAccessToken);

                    // Retry the original request with the new token
                    originalRequest.headers["Authorization"] = `Bearer ${newAccessToken}`;
                    return authAPI(originalRequest);
                } catch (refreshError) {
                    handleLogout();
                    return Promise.reject(refreshError);
                }
            }
            return Promise.reject(error);
        }
    );
};

// User Login
export async function loginUser({ username, password }) {
    try {
        const response = await axios.post(`${AUTHENTICATION_API}/user/login`, { username, password });
        return response.data;
    } catch (error) {
        if (error.response?.status === 401) {
            throw new Error("Username/Password invalid");
        }
        throw error;
    }
}

// User Logout
export async function logoutUser() {
    const token = localStorage.getItem("accessToken");
    try {
        const response = await authAPI.post("/user/logout", {
            headers: {
                "Authorization": `Bearer ${JSON.parse(token)}`
            }
        });
        return response.data;
    } catch (error) {
        console.error("Error logging out:", error);
        throw error;
    }
}

// Get Current User
export async function getUser() {
    try {
        const response = await authAPI.get("/user/",
            {
                headers: getAuthHeaders()
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error fetching user data:", error.response || error);
        throw error;
    }
}
export async function updateUser(userData, file) {
    const formData = new FormData();
    // Loop through the object keys and append them dynamically
    Object.keys(userData).forEach((key) => {
        if (typeof userData[key] === "object" && userData[key] !== null) {
            // If the value is an object (e.g., dob), append nested fields
            Object.keys(userData[key]).forEach((subKey) => {
                formData.append(`${key}.${subKey}`, userData[key][subKey]);
            });
        } else {
            // Append normal key-value pairs
            formData.append(key, userData[key]);
        }
    });
    console.log(file)
    // Append file separately
    if (file) {
        formData.append("avt", file);
    }
    const token = localStorage.getItem("accessToken");
    try {
        const response = await authAPI.post("/user/update", formData,
            {
                headers: {
                    "X-Requested-With": "XMLHttpRequest",
                    "Content-Type": "multipart/form-data",
                    "Authorization": `Bearer ${JSON.parse(token)}`
                }
            },
        );
        return response.data;
    } catch (error) {
        console.error("Error updating user data:", error.response || error);
        throw error;
    }
}
export async function changePassword({ oldPassword, newPassword, confirmPassword, token }) {
    try {
        const response = await authAPI.post(
            "/user/password",
            { oldPassword, newPassword, confirmPassword }, // This is the body of the request
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );
        return response.data;
    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export async function getAddress(token) {
    try {
        const response = await authAPI.get(
            "/user/address",
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );
        return response.data;

    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export async function updateAddress({ id, city, district, ward, streetNumber, firstName, lastName, phoneNumber }, token) {
    try {
        const response = await authAPI.put(
            "/user/address",
            { id, city, district, ward, streetNumber, firstName, lastName, phoneNumber },
            {

                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );
        return response.data;

    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export async function createAddress({ city, district, ward, streetNumber, firstName, lastName, phoneNumber }, token) {
    try {
        const response = await authAPI.post(
            "/user/address",
            { city, district, ward, streetNumber, firstName, lastName, phoneNumber },
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );
        return response.data;

    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export async function deleteAddress(addressId, token) {
    try {
        const response = await authAPI.delete(
            `/user/address?addressId=${addressId}`,
            {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            }
        );
        return response.data;

    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export { authAPI };