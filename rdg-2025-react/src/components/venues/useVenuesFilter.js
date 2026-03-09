import { useTableFilters } from "../common/Table/useTableFilters.js";
import { venueFilters } from "../common/Table/filters/venues.filters.js";
import { useVenues } from "../../hooks/useVenues.js";

export const useVenuesFilter = () => {
  const { venues } = useVenues();

  const { filteredItems, filtersForUI } = useTableFilters({
    items: venues.data,
    filterDefs: venueFilters,
  });

  return {
    filteredVenues: filteredItems,
    filtersForUI,
    loading: venues.isLoading,
  };
};
