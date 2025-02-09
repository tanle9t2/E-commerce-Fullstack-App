const CART_API = 'http://localhost:8080/ecommerce-server/api/v1/cart'
export async function getCart(cartId) {
    const res = await fetch(`${CART_API}?cartId=${cartId}`);

    // fetch won't throw error on 400 errors (e.g. when URL is wrong), so we need to do it manually. This will then go into the catch block, where the message is set
    if (!res.ok) throw Error("Failed getting cart");
  
    const data  = await res.json();
    return data;
}
export async function addCartItem(cartItem) {
    const res = await fetch(`${CART_API}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cartItem),
      });
      const result = await res.json();
      return result;
}
export async function deleteCartItem({ cartId, cartItems }) {
  const data = { cartItems: cartItems };
  try {
      const res = await fetch(`${CART_API}/cartItem?cartId=${cartId}`, {
          method: "DELETE",
          headers: {
              "Content-Type": "application/json",
          },
          body: JSON.stringify(data),
      });

      if (!res.ok) {
          const errorText = await res.text(); // Try reading the response text
          throw new Error(`Failed deleting cart item: ${res.status} - ${res.statusText} - ${errorText}`);
      }

      return await res.json();
  } catch (error) {
      console.error("Error deleting cart item:", error);
      throw error; // Re-throw to handle it elsewhere
  }
}
export async function updateCartItem(cartItem) {
    const res = await fetch(`${CART_API}/cartItem`, {
        method: "PUT",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(cartItem),
      });
      if (!res.ok) throw Error("Failed updating cart item");
      const result = await res.json();
      return result;
}