import api from "./api";




export async function getProducts() {
    try {
        const res = await api.get("/product_list");

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getProductsOfTenant(tenantId) {
    try {
        const res = await api.get(`/product/tenant/${tenantId}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}

export async function getProduct(id) {
    try {
        const res = await api.get(`/product/${id}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getVariationInfor(id) {
    try {
        const res = await api.get(`/product/sku/${id}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getComments({ productId, page = 0 }) {
    try {
        const res = await api.get(`/comment/product/${productId}?page=${page}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}