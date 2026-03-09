import { productionsFilters } from "../common/Table/filters/productions.filters.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";
import { useProductions } from "../../hooks/useProductions.js";

export const useProductionsFilter = () => {
  const { productions } = useProductions();

  const { filteredItems, filtersForUI } = useTableFilters({
    items: productions.data,
    filterDefs: productionsFilters,
  });

  return {
    filteredProductions: filteredItems,
    filtersForUI,
    loading: productions.isLoading,
  };
};
