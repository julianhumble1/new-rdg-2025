import { useEffect, useState } from "react";
import EventService from "../../services/EventService.js";
import DataTable from "react-data-table-component";
import Table from "../common/Table/Table.jsx";
import TableLink from "../common/Table/TableLink.jsx";

const AllEventsTable = () => {
  const [events, setEvents] = useState([]);

  const columns = [
    {
      name: "Name",
      selector: (row) => row.name,
      cell: (row) => <TableLink link={`/events/${row.id}`} text={row.name} />,
    },
    {
      name: "Description",
      selector: (row) => row.description,
    },
    {
      name: "Date & Time",
      selector: (row) => row.dateTime,
    },
    {
      name: "Venue",
      selector: (row) => (
        <TableLink link={`/venues/${row.venue?.id}`} text={row.venue?.name} />
      ),
    },
  ];

  useEffect(() => {
    const fetchAllEvents = async () => {
      const response = await EventService.getAllEvents();
      setEvents(response.data.events);
    };
    fetchAllEvents();
  }, []);

  return <Table data={events} columns={columns} />;
};

export default AllEventsTable;
