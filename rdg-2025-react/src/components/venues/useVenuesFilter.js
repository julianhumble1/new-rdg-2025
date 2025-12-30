import { useCallback } from "react";
import VenueService from "../../services/VenueService.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";
import { venueFilters } from "../common/Table/filters/venues.filters.js";

export const useVenuesFilter = () => {
  const fetcher = useCallback(
    async () =>
      VenueService.getAllVenues().then((res) => res.data.venues ?? []),
    [],
  );

  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
    filterDefs: venueFilters,
  });

  return {
    filteredVenues: filteredItems,
    filtersForUI,
    loading,
  };
};
