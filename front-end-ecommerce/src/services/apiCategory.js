import { createAPI } from "./api";

const CATEGORY_API = "http://localhost:8080/ecommerce-server/api/v1/category";

const categoryAPI = createAPI(CATEGORY_API)

export async function getCategoryByTenant(id) {
    try {
        const res = await categoryAPI.get(`/tenant?tenantId=${id}`);
        return res.data;
    } catch (error) {
        console.error("Failed getting category:", error);
        throw new Error("Failed getting cart");
    }
}