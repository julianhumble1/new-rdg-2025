export const festivalsFilters = [
  {
    key: "name",
    placeholder: "Festival Name",
    accessor: (festival) => festival?.name ?? "",
  },
  {
    key: "venue",
    placeholder: "Venue Name",
    accessor: (festival) => festival?.venue?.name ?? "",
  },
];
