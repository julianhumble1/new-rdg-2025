import { useCallback, useState } from "react";
import PersonService from "../../services/PersonService.js";
import { peopleFilters } from "../common/Table/filters/people.filters.js";
import { useTableFilters } from "../common/Table/useTableFilters.js";

export const usePeopleFilter = () => {
  const [responseType, setResponseType] = useState("PUBLIC");

  const fetcher = useCallback(
    async () =>
      PersonService.getAllPeople().then((res) => {
        setResponseType(res.data.responseType);
        return res.data.people ?? [];
      }),
    [],
  );

  const { filteredItems, filtersForUI, loading } = useTableFilters({
    fetcher,
    filterDefs: peopleFilters,
  });

  return {
    filteredPeople: filteredItems,
    filtersForUI,
    loading,
    responseType,
  };
};
