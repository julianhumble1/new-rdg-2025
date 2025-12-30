import PeopleTable from "./PeopleTable.jsx";
import CustomSpinner from "../common/CustomSpinner.jsx";
import { usePeopleFilter } from "./usePeopleFilter.js";
import FiltersTable from "../common/FiltersTable.jsx";

const AllPeople = () => {
  const { filteredPeople, filtersForUI, loading, responseType } =
    usePeopleFilter();

  return (
    <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        {loading ? (
          <CustomSpinner />
        ) : (
          <PeopleTable people={filteredPeople} responseType={responseType} />
        )}
      </div>
    </div>
  );
};

export default AllPeople;
