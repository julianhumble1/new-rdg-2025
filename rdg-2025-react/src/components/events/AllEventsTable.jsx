import { useEffect, useState } from "react";
import EventService from "../../services/EventService.js";
import Table from "../common/Table/Table.jsx";
import { eventsColumns } from "../common/Table/columns/events.columns.jsx";

const AllEventsTable = () => {
  const [events, setEvents] = useState([]);

  useEffect(() => {
    const fetchAllEvents = async () => {
      const response = await EventService.getAllEvents();
      setEvents(response.data.events);
    };
    fetchAllEvents();
  }, []);

  return <Table data={events} columns={eventsColumns} />;
};

export default AllEventsTable;
