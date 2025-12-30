import { useCallback } from "react";
import ProductionService from "../../services/ProductionService.js";
import { productionsFilters } from "../common/Table/filters/productions.filters.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";

export const useProductionsFilter = () => {
  const fetcher = useCallback(async () =>
    ProductionService.getAllProductions().then(
      (res) => res.data.productions ?? [],
    ), []);
  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
    filterDefs: productionsFilters,
  });

  return {
    filteredProductions: filteredItems,
    filtersForUI,
    loading,
  };
};
