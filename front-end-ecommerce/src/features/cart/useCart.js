import { useQuery } from "@tanstack/react-query";
import { getCart } from "../../services/apiCart";
import { useAuthContext } from "../../context/AuthContext";



export function useCart() {
  const { auth } = useAuthContext()
  const {
    isLoading,
    data: cart,
    error,
  } = useQuery({
    queryKey: ["cart", auth.id],
    queryFn: () => getCart(),
  });
  return { isLoading, error, cart };
}