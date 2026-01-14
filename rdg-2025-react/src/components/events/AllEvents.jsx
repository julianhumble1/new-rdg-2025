import EventsTable from "./EventsTable.jsx";
import FiltersTable from "../common/FiltersTable.jsx";
import { useEventsFilter } from "./useEventsFilter.js";
import CustomSpinner from "../common/CustomSpinner.jsx";

const AllEvents = () => {
  const { filteredEvents, filtersForUI, loading } = useEventsFilter();

  return (
    <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        {loading ? <CustomSpinner /> : <EventsTable events={filteredEvents} />}
      </div>
    </div>
  );
};

export default AllEvents;
