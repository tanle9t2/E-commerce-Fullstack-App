const CART_API = 'http://localhost:8080/ecommerce-server/api/v1/cart'
export async function getCart(cartId) {
    const res = await fetch(`${CART_API}?cartId=${cartId}`);

    // fetch won't throw error on 400 errors (e.g. when URL is wrong), so we need to do it manually. This will then go into the catch block, where the message is set
    if (!res.ok) throw Error("Failed getting cart");
  
    const data  = await res.json();
    return data;
}