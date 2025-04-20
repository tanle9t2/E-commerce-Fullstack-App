import api, { axiosPrivate } from "./api";
export async function searchProduct({ keyword, tenantId, location, category, minPrice, maxPrice, lft, rgt, sortBy, order, size, page }) {
    const params = {};

    if (keyword) params.keyword = keyword;
    if (category) params.category = category;
    if (minPrice) params.minPrice = minPrice;
    if (maxPrice) params.maxPrice = maxPrice;
    if (sortBy) params.sortBy = sortBy;
    if (order) params.order = order;
    if (size) params.size = size;
    if (location) params.location = location;
    if (lft) params.lft = lft;
    if (rgt) params.rgt = rgt;
    if (tenantId) params.tenantId = tenantId
    if (page !== undefined) params.page = page; // Include page if defined (even if 0)  

    try {
        const res = await api.get("/search", { params });

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function searchHint(keyword) {
    try {
        const res = await api.get(`/search-hint?keyword=${keyword}`,);

        return res.data;
    } catch (error) {
        console.error("Failed getting hint:", error);
        throw new Error("Failed getting cart");
    }
}
export async function getFilterSearch(keyword) {

    try {
        const res = await api.get(`/filter-search?keyword=${keyword}`,);

        return res.data;
    } catch (error) {
        console.error("Failed getting filter search:", error);
        throw new Error("Failed getting cart");
    }

}