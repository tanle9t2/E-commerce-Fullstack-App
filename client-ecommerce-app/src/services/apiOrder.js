import { axiosPrivate } from "./api";

export async function getOrders(token) {
    try {
        const res = await axiosPrivate.get("/order/purchase", {
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
export async function createOrder(data, token) {
    console.log(data)
    try {
        const res = await axiosPrivate.post("/order", data, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        return res.data;
    } catch (error) {
        console.error("Failed create order:", error);
        throw new Error("Failed create order");
    }
}