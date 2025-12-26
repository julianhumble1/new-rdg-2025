export const productionsFilters = [
  {
    key: "name",
    placeholder: "Production Name",
    accessor: (production) => production?.name ?? "",
  },
  {
    key: "venue",
    placeholder: "Venue Name",
    accessor: (production) => production?.venue?.name ?? "",
  },
];
