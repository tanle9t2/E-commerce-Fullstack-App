

import api, { axiosPrivate } from "./api";


// User Login
export async function loginUser({ username, password }) {
    try {
        const response = await api.post(`/user/login`, { username, password });
        return response.data;
    } catch (error) {
        if (error.response?.status === 401) {
            throw new Error("Username/Password invalid");
        }
        throw error;
    }
}
export async function signUp({ firstName, lastName, phoneNumber, email, username, password }) {
    try {
        const response = await api.post(`/user/register`, { firstName, lastName, phoneNumber, email, username, password });
        return response.data;
    } catch (error) {
        if (error.response?.status === 409) {
            throw new Error("Username đã tồn tài");
        }
        throw error;
    }
}

// User Logout
export async function logoutUser() {
    const token = localStorage.getItem("accessToken");
    try {
        const response = await axiosPrivate.post("/user/logout", {
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

        const response = await axiosPrivate.get("/user/")
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
        } else if (userData[key]) {
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
        const response = await axiosPrivate.post("/user/update", formData,
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
        const response = await axiosPrivate.post(
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
export async function getAddress() {
    try {
        const response = await axiosPrivate.get(
            "/user/address"
        );
        return response.data;

    } catch (error) {
        console.error("Error changing user data:", error.response || error);
        throw error;
    }
}
export async function updateAddress({ id, city, district, ward, streetNumber, firstName, lastName, phoneNumber }, token) {
    try {
        const response = await axiosPrivate.put(
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
        const response = await axiosPrivate.post(
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
        const response = await axiosPrivate.delete(
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
