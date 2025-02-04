import { useQuery } from "@tanstack/react-query";
import { getCart } from "../../services/apiCart";


export function useCart() {
    const cartId =1;
     const {
        isLoading,
        data: cart,
        error,
      } = useQuery({
        queryKey: ["cart",cartId],
        queryFn: () => getCart(cartId),
      });
      return { isLoading, error, cart };
}