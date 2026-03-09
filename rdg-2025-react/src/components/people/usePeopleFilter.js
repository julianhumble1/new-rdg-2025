import { peopleFilters } from "../common/Table/filters/people.filters.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";
import { usePeople } from "../../hooks/usePeople.js";

export const usePeopleFilter = () => {
  const { people, responseType } = usePeople();

  const { filteredItems, filtersForUI } = useTableFilters({
    items: people.data,
    filterDefs: peopleFilters,
  });

  return {
    filteredPeople: filteredItems,
    filtersForUI,
    loading: people.isLoading,
    responseType,
  };
};
