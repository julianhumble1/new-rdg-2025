import ProductionsTable from "./ProductionsTable.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";
import { useProductionsFilter } from "./useProductionsFilter.js";
import FiltersTable from "../common/FiltersTable.jsx";

const AllProductions = () => {
  const { filteredProductions, filtersForUI, loading } = useProductionsFilter();

  return (
    <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        {loading ? (
          <CustomSpinner />
        ) : (
          <ProductionsTable productions={filteredProductions} />
        )}
      </div>
    </div>
  );
};

export default AllProductions;
