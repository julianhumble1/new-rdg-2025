import { useTableFilters } from "../common/Table/useTableFilters.js";
import EventService from "../../services/EventService";
import { eventsFilters } from "../common/Table/filters/events.filters.js";
import { useCallback } from "react";

// thin wrapper that wires the generic hook to the events service + defs
export const useEventsFilter = () => {
  const fetcher = useCallback( async () =>
    EventService.getAllEvents().then((res) => res.data.events ?? []), []);

  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
    filterDefs: eventsFilters,
  });

  return {
    filteredEvents: filteredItems,
    filtersForUI,
    loading,
  };
};
