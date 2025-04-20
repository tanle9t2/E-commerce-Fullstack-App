import api from "./api";

export async function getProductsOfTenant({ tenantId, page }) {
    console.log(page)
    try {
        const res = await api.get(`/product/tenant/${tenantId}`, {
            params: { page }  // ✅ Correct way to pass query params
        });

        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}
export async function createProduct(data) {
    const formData = new FormData();

    // Convert objects to JSON strings
    formData.append("product", JSON.stringify(data.product));
    formData.append("options", JSON.stringify(data.options));
    formData.append("skus", JSON.stringify(data.skus));
    console.log(data.images)
    // Append image files correctly
    data.images.forEach((file) => {
        console.log(file)
        formData.append("images", file.file); // If backend expects an array, use "images[]"
    });

    try {
        const res = await api.post(`/product`, formData, {
            headers: {
                "Content-Type": "multipart/form-data"
            }
        });
        return res.data;
    } catch (error) {
        console.error("Failed getting product:", error);
        throw new Error("Failed getting cart");
    }
}