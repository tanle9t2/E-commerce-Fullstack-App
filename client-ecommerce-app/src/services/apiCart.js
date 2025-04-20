
import { getAuthHeaders } from "../utils/helper"
import { axiosPrivate } from "./api";




// Get cart by ID
export async function getCart() {
    try {
        const res = await axiosPrivate.get("/cart");

        return res.data;
    } catch (error) {
        console.error("Failed getting cart:", error);
        throw new Error("Failed getting cart");
    }
}

// Add cart item
export async function addCartItem({ skuId, quantity }) {
    try {
        const res = await axiosPrivate.post("/cart", { skuId, quantity }, {
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
        const res = await axiosPrivate.delete("/cart/cartItem", {
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
        const res = await axiosPrivate.put("/cart/cartItem", cartItem, {
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
