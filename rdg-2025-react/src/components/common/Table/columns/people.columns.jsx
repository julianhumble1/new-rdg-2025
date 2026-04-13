import TableLink from "../TableLink.jsx";

export const getPeopleColumns = (responseType) => {
  if (responseType === "PUBLIC") return publicPeopleColumns;

  if (responseType === "DETAILED") {
    // remove summary for detailed view
    const withoutSummary = publicPeopleColumns.filter(
      (column) => column.name !== "Summary",
    );

    return withoutSummary.concat(detailedPeopleColumns);
  }
};

const publicPeopleColumns = [
  {
    name: "Name",
    selector: (row) => `${row?.firstName} ${row?.lastName}`,
    cell: (row) => (
      <TableLink
        link={`/people/${row.id} `}
        text={`${row?.firstName} ${row?.lastName}`}
      />
    ),
  },
  {
    name: "Summary",
    selector: (row) => row?.summary,
    cell: (row) => (
      <div className="text-gray-600 line-clamp-2">{row?.summary}</div>
    ),
  },
];

const detailedPeopleColumns = [
  {
    name: "Phone",
    selector: (row) => (
      <ul>
        <li>{row?.homePhone}</li>
        <li>{row?.mobilePhone}</li>
      </ul>
    ),
  },
  {
    name: "Address",
    selector: (row) => (
      <ul>
        <li>{row?.addressStreet}</li>
        <li>{row?.addressTown}</li>
        <li>{row?.addressPostcode}</li>
      </ul>
    ),
  },
];
