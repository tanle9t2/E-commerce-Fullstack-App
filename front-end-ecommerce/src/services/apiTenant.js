const TENANT_API_URL = "http://localhost:8080/ecommerce-server/api/v1/tenant";
export async function getTenant(id) {
    const res = await fetch(`${TENANT_API_URL}/tenant-infor/${id}`);

    // fetch won't throw error on 400 errors (e.g. when URL is wrong), so we need to do it manually. This will then go into the catch block, where the message is set
    if (!res.ok) throw Error("Failed getting tenant");
    const data = await res.json();
    return data;
}
export async function getTenants(id) {
    const res = await fetch(`${TENANT_API_URL}/${id}`);

    // fetch won't throw error on 400 errors (e.g. when URL is wrong), so we need to do it manually. This will then go into the catch block, where the message is set
    if (!res.ok) throw Error("Failed getting tenant");
    const data = await res.json();
    return data;
}