import axios from "axios";

// Function to create an Axios instance for a given base URL
export function createAPI(baseURL) {
    const instance = axios.create({
        baseURL,
        headers: { "Content-Type": "application/json" },
        withCredentials: true,
    });

    // Function to set the JWT token dynamically
    instance.setAuthToken = (token) => {
        if (token) {
            instance.defaults.headers["Authorization"] = `Bearer ${token}`;
        } else {
            console.log("ok")
            delete instance.defaults.headers["Authorization"];
        }
    };

    return instance;
}
