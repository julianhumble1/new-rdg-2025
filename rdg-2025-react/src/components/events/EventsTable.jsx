import Table from "../common/Table/Table.jsx";
import { eventsColumns } from "../common/Table/columns/events.columns.jsx";

const EventsTable = ({ events }) => {
  return <Table data={events} columns={eventsColumns} />;
};

export default EventsTable;
