import { createAPI } from "./api";
const ORDER_API = `http://localhost:8080/ecommerce-server/api/v1/order`
const orderAPI = createAPI(ORDER_API)

export async function getOrders(token) {
    try {
        const res = await orderAPI.get("/purchase", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        return res.data;
    } catch (error) {
        console.error("Failed getting order:", error);
        throw new Error("Failed getting cart");
    }
}