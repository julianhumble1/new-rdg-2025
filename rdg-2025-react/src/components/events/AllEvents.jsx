import EventsTable from "./EventsTable.jsx";
import FiltersTable from "../common/FiltersTable.jsx";
import { useEventsFilter } from "./useEventsFilter.js";

const AllEvents = () => {
  const { filteredEvents, filtersForUI } = useEventsFilter();

  return (
    <div className="flex flex-col sm:flex-row">
      <FiltersTable filters={filtersForUI} />
      <div className="flex-1">
        <EventsTable events={filteredEvents} />
      </div>
    </div>
  );
};

export default AllEvents;
