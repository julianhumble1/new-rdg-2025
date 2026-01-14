export const peopleFilters = [
  {
    key: "name",
    placeholder: "Name",
    accessor: (person) => (person?.firstName ?? "") + (person?.lastName ?? ""),
  },
];
