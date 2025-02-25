
import { createAPI } from "./api";

const PRODUCT_API_URL = "http://localhost:8080/ecommerce-server/api/v1/";
const COMMENT_API_URL = "http://localhost:8080/ecommerce-server/api/v1/comment";
const SEARCH_API_URL = "http://localhost:8080/ecommerce-server/api/v1/search";

const productAPI = createAPI(PRODUCT_API_URL)
const commentAPI = createAPI(COMMENT_API_URL)
const searchAPI = createAPI(SEARCH_API_URL)
export async function getProducts() {
    try {
        const res = await productAPI.get("/product_list");

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function searchProduct({ keyword, category, minPrice, maxPrice, sortBy, order, page }) {
    const params = {};
    if (keyword) params.keyword = keyword;
    if (category) params.category = category;
    if (minPrice) params.minPrice = minPrice;
    if (maxPrice) params.maxPrice = maxPrice;
    if (sortBy) params.sortBy = sortBy;
    if (order) params.order = order;
    if (page !== undefined) params.page = page; // Include page if defined (even if 0)

    try {
        const res = await searchAPI.get("", { params });

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