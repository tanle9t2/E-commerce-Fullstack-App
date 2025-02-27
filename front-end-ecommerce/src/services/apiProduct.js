
import { createAPI } from "./api";

const PRODUCT_API_URL = "http://localhost:8080/ecommerce-server/api/v1/";
const COMMENT_API_URL = "http://localhost:8080/ecommerce-server/api/v1/comment";


const productAPI = createAPI(PRODUCT_API_URL)
const commentAPI = createAPI(COMMENT_API_URL)

export async function getProducts() {
    try {
        const res = await productAPI.get("/product_list");

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getProductsOfTenant(tenantId) {
    try {
        const res = await productAPI.get(`/product/tenant/${tenantId}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}

export async function getProduct(id) {
    try {
        const res = await productAPI.get(`/product/${id}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getVariationInfor(id) {
    try {
        const res = await productAPI.get(`/product/sku/${id}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getComments({ productId, page = 0 }) {
    try {
        const res = await commentAPI.get(`/product/${productId}?page=${page}`);

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}