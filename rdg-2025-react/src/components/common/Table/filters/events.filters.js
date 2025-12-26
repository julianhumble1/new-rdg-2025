export const eventsFilters = [
  {
    key: "name",
    placeholder: "Event Name",
    accessor: (event) => event?.name ?? "",
  },
  {
    key: "venue",
    placeholder: "Venue Name",
    accessor: (event) => event.venue?.name ?? "",
  },
];
