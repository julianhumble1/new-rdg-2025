import TableLink from "../../TableLink.jsx";
import Cookies from "js-cookie";
import { CreditsActionCell } from "./CreditsActionCell.jsx";

// TODO: make this a method that if they are admin adds the action buttons

export const getCreditsColumns = () => {
  const role = Cookies.get("role");

  if (role === "ROLE_ADMIN") {
    return creditsColumns.concat(creditsActionButtons)
  } else {
    return creditsColumns
  }
};

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
];

const creditsActionButtons = [{
  name: "Actions",
  cell: (row) => <CreditsActionCell row={row} />
}];


