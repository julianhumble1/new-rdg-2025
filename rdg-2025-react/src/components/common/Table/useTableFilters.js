import { useEffect, useMemo, useState } from "react";

/**
 * Generic table filtering hook.
 * - items: items []
 * - filterDefs: [{ key, placeholder, accessor: (item) => string }]
 */

const debounceMs = 250;

export const useTableFilters = ({ items = [], filterDefs }) => {
  const initialFilters = Object.fromEntries(
    filterDefs.map((fd) => [fd.key, ""]),
  );

  // const [items, setItems] = useState([]);
  const [filters, setFilters] = useState(initialFilters);
  const [debouncedFilters, setDebouncedFilters] = useState(initialFilters);

  useEffect(() => {
    const t = setTimeout(() => setDebouncedFilters(filters), debounceMs);
    return () => clearTimeout(t);
  }, [filters]);

  const setFilter = (key, value) =>
    setFilters((prev) => ({ ...prev, [key]: value }));

  const filteredItems = useMemo(() => {
    if (!Object.values(debouncedFilters).some(Boolean)) return items;
    return items.filter((item) => {
      for (const fd of filterDefs) {
        const q = (debouncedFilters[fd.key] || "").toLowerCase();
        if (!q) continue;
        const value = (fd.accessor(item) || "").toString().toLowerCase();
        if (!value.includes(q)) return false;
      }
      return true;
    });
  }, [items, debouncedFilters, filterDefs]);

  const filtersForUI = filterDefs.map((fd) => ({
    placeholder: fd.placeholder,
    value: filters[fd.key],
    onChange: (e) => setFilter(fd.key, e.target.value),
  }));

  return {
    items,
    filteredItems,
    filters,
    setFilter,
    filtersForUI,
  };
};
