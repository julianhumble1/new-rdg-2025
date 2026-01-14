import { useCallback } from "react";
import FestivalService from "../../services/FestivalService.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";
import { festivalsFilters } from "../common/Table/filters/festivals.filters.js";

export const useFestivalsFilter = () => {
  const fetcher = useCallback(
    async () =>
      FestivalService.getAllFestivals().then((res) => res.data.festivals ?? []),
    [],
  );

  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
    filterDefs: festivalsFilters,
  });

  return {
    filteredFestivals: filteredItems,
    filtersForUI,
    loading,
  };
};
