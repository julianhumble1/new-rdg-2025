import TableLink from "../TableLink.jsx";

export const festivalColumns = [
  {
    name: "Festival",
    selector: (row) => row.name.toLowerCase(),
    cell: (row) => <TableLink link={`/festivals/${row.id}`} text={row.name} />,
  },
  {
    name: "Year",
    selector: (row) => row.year,
    sortable: true,
  },
  {
    name: "Venue",
    selector: (row) => row?.venue?.name.toLowerCase(),
    cell: (row) => (
      <TableLink
        link={`/venues/${row?.venue?.id}`}
        text={row?.venue?.name ?? ""}
      />
    ),
    sortable: true,
  },
  {
    name: "Description",
    selector: (row) => row?.description,
    cell: (row) => (
      <div className="text-gray-600 line-clamp-3">{row.description}</div>
    ),
  },
];
