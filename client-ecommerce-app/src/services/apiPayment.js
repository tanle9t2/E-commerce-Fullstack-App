import { axiosPrivate } from "./api"


export async function getMethods(token) {
    try {
        const res = await axiosPrivate.get("/payment/methods", {
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
export async function getPaymentUrl(bankCode, orderId) {
    try {
        const res = await axiosPrivate.get(`/payment?bankCode=${bankCode}&orderId=${orderId}`)
        return res.data;
    } catch (error) {
        console.error("Failed getting payment url", error);
        throw new Error("Failed getting payment url");
    }
}