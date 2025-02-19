import { useQuery } from "@tanstack/react-query";
import { getCart } from "../../services/apiCart";
import { useAuthContext } from "../../context/AuthContext";


export function useCart() {
  const { token, user } = useAuthContext()
  const {
    isLoading,
    data: cart,
    error,
  } = useQuery({
    queryKey: ["cart", user.id],
    queryFn: () => getCart(token),
  });
  return { isLoading, error, cart };
}