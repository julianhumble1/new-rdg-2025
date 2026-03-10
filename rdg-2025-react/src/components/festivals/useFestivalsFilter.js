import { useTableFilters } from "../common/Table/useTableFilters.js";
import { festivalsFilters } from "../common/Table/filters/festivals.filters.js";
import { useFestivals } from "../../hooks/useFestivals.js";

export const useFestivalsFilter = () => {
  const { festivals } = useFestivals();

  const { filteredItems, filtersForUI } = useTableFilters({
    items: festivals.data,
    filterDefs: festivalsFilters,
  });

  return {
    filteredFestivals: filteredItems,
    filtersForUI,
    loading: festivals.isLoading,
  };
};
