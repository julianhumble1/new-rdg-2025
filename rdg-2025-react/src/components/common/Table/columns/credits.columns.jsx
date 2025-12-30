import TableLink from "../TableLink.jsx";
import Cookies from "js-cookie";

// TODO: make this a method that if they are admin adds the action buttons

const role = Cookies.get("role");

export const creditsColumns = [
  {
    name: "Credit",
    selector: (row) => (
      <ul>
        <li>{row?.name}</li>
        <li className="text-gray-600">{row?.summary}</li>
      </ul>
    ),
  },
  {
    name: "Played By",
    selector: (row) => row?.person,
    cell: (row) => (
      <TableLink
        link={`/people/${row?.person?.id}`}
        text={`${row.person?.firstName} ${row.person?.lastName}`}
      />
    ),
  },
  {
    name: "Production",
    selector: (row) => row?.production,
    cell: (row) => (
      <TableLink
        link={`/productions/${row?.production?.id}`}
        text={row.production?.name}
      />
    ),
  },
  {
    name: "Actions",
    cell: (row) => <div>{role}</div>,
  },
];
