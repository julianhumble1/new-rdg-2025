import { useCallback } from "react";
import UserService from "../../services/UserService.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";

export const useUsersFilter = () => {
  const fetcher = useCallback(
    async () => UserService.getAllUsers().then((res) => res.data.users ?? []),
    [],
  );

  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
  });

  return { filteredUsers: filteredItems, filtersForUI, loading };
};
