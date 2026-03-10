import { useTableFilters } from "../common/Table/useTableFilters.js";
import { eventsFilters } from "../common/Table/filters/events.filters.js";
import { useEvents } from "../../hooks/useEvents.js";

// thin wrapper that wires the generic hook to the events service + defs
export const useEventsFilter = () => {
  const { events } = useEvents();

  const { filteredItems, filtersForUI } = useTableFilters({
    items: events.data,
    filterDefs: eventsFilters,
  });

  return {
    filteredEvents: filteredItems,
    filtersForUI,
    loading: events.isLoading,
  };
};
