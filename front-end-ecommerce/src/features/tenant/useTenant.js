import { useQuery } from "@tanstack/react-query";
import { getTenant } from "../../services/apiTenant";

export default function useTenant(tenantId) {
  const {
    isLoading,
    data: tenant,
    error,
  } = useQuery({
    queryKey: ["tenantId", tenantId],
    queryFn: () => getTenant(tenantId),
  });

  return { isLoading, error, tenant };
}