import { format, parseISO } from "date-fns";
import TableLink from "../TableLink.jsx";

export const eventsColumns = [
  {
    name: "Name",
    selector: (row) => row.name.toLowerCase(),
    cell: (row) => <TableLink link={`/events/${row.id}`} text={row.name} />,
    sortable: true,
  },
  {
    name: "Description",
    selector: (row) => row.description,
  },
  {
    name: "Date & Time",
    selector: (row) =>
      row.dateTime ? format(parseISO(row.dateTime), "dd/MM/yyyy h:mm a") : "—",
    sortable: true,
  },
  {
    name: "Venue",
    cell: (row) => (
      <TableLink link={`/venues/${row.venue?.id}`} text={row.venue?.name} />
    ),
    selector: (row) => row.venue?.name.toLowerCase() ?? "",
    sortable: true,
  },
];
