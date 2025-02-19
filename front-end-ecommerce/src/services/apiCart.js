
import { createAPI } from "./api";
import { getAuthHeaders } from "../utils/helper"
const CART_API = "http://localhost:8080/ecommerce-server/api/v1/cart";

const cartAPI = createAPI(CART_API)

// Get cart by ID
export async function getCart(token) {
    try {
        const res = await cartAPI.get("", {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        return res.data;
    } catch (error) {
        console.error("Failed getting cart:", error);
        throw new Error("Failed getting cart");
    }
}

// Add cart item
export async function addCartItem({ skuId, quantity }) {
    try {
        const res = await cartAPI.post("", { skuId, quantity }, {
            headers: getAuthHeaders()
        });
        return res.data;
    } catch (error) {
        console.error("Failed adding cart item:", error);
        throw new Error("Failed adding cart item");
    }
}

// Delete cart item
export async function deleteCartItem({ cartItems, token }) {
    try {
        const res = await cartAPI.delete("/cartItem", {
            data: { cartItems }, // Attach body properly
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (error) {
        console.error("Failed deleting cart item:", error);
        throw new Error("Failed deleting cart item");
    }
}

// Update cart item
export async function updateCartItem(cartItem, token) {
    try {
        const res = await cartAPI.put("/cartItem", cartItem, {
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });
        return res.data;
    } catch (error) {
        console.error("Failed updating cart item:", error);
        throw new Error("Failed updating cart item");
    }
}
