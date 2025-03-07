import { axiosPrivate } from "./api";



export async function getCategoryByTenant(id) {
    try {
        const res = await axiosPrivate.get(`/tenant?tenantId=${id}`);
        return res.data;
    } catch (error) {
        console.error("Failed getting category:", error);
        throw new Error("Failed getting cart");
    }
}