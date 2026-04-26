import Cookies from "js-cookie";
import TableLink from "../../TableLink.jsx";
import { AwardsActionCell } from "./AwardsActionCell.jsx";

export const getAwardsColumns = (onAfterDelete) => {
    const role = Cookies.get("role");

    if (role === "ROLE_ADMIN" || role === "ROLE_SUPERADMIN") {
        return awardsColumns.concat([
            {
                name: "Actions",
                cell: (row) => <AwardsActionCell row={row} onAfterDelete={onAfterDelete} />,
            },
        ]);
    } else {
        return awardsColumns;
    }
};

export const awardsColumns = [
    {
        name: "Award",
        selector: (row) => row?.name,
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
        name: "Festival",
        selector: (row) => row?.festival,
        cell: (row) => (
            <TableLink
                link={`/festivals/${row?.festival?.id}`}
                text={row.festival?.name}
            />
        ),
    },
    {
        name: "Person",
        selector: (row) => row?.person,
        cell: (row) => (
            <TableLink
                link={`/people/${row?.person?.id}`}
                text={`${row.person?.firstName} ${row.person?.lastName}`}
            />
        ),
    },
];
