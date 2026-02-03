export const customStyles = {
  header: {
    style: {
      padding: "0.75rem 1rem",
    },
  },
  headRow: {
    style: {
      backgroundColor: "#cbd5e1", // tailwind slate-100
      borderBottomWidth: "1px",
      borderBottomColor: "#e2e8f0", // slate-200
    },
  },
  // header titles
  headCells: {
    style: {
      fontSize: "1.1rem",
      fontWeight: "700",
      paddingLeft: "1rem",
      paddingRight: "1rem",
      textTransform: "uppercase",
    },
  },
  rows: {
    style: {
      minHeight: "70px",
      "&:not(:last-of-type)": {
        borderBottomStyle: "solid",
        borderBottomWidth: "1px",
        borderBottomColor: "#e2e8f0",
      },
      fontSize: "1rem",
      transition: "background-color 150ms ease",
      "&:hover": {
        backgroundColor: "#008000"
      }
    },
  },
  cells: {
    style: {
      paddingLeft: "1rem",
      paddingRight: "1rem",
    },
  },
  pagination: {
    style: {
      borderTopWidth: "1px",
      borderTopColor: "#e2e8f0",
    },
  },
};
