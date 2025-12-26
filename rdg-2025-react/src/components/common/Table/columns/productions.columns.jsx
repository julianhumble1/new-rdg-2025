import TableLink from "../TableLink.jsx";

export const productionsColumns = [
  {
    name: "Production",
    selector: (row) => row.name.toLowerCase(),
    cell: (row) => (
      <TableLink link={`/productions/${row.id}`} text={row.name} />
    ),
    sortable: true,
  },
  {
    name: "Venue",
    selector: (row) => row?.venue?.name.toLowerCase(),
    cell: (row) => (
      <TableLink link={`/venues/${row.id}`} text={row?.venue?.name ?? ""} />
    ),
    sortable: true,
  },
  {
    name: "Author",
    selector: (row) => row?.author,
    sortable: true,
  },
];
