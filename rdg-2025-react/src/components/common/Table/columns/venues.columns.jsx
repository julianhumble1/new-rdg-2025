import TableLink from "../TableLink.jsx";

export const venueColumns = [
  {
    name: "Name",
    selector: (row) => row.name.toLowerCase(),
    cell: (row) => <TableLink link={`/venues/${row.id}`} text={row.name} />,
    sortable: true,
  },
  {
    name: "Address",
    selector: (row) => row?.address,
    cell: (row) => (
      <ul className="text-gray-600">
        <li>{row?.address}</li>
        <li>{row?.town}</li>
        <li>{row?.postcode}</li>
      </ul>
    ),
  },
];
