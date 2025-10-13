import DataTable from "react-data-table-component";
import { customStyles } from "./styles.js";

const Table = ({ data, columns }) => {
  return (
    <div className="rounded shadow-xl p-2 min-h-screen">
      <DataTable
        columns={columns}
        data={data}
        customStyles={customStyles}
        pagination
        highlightOnHover
        responsive
      />
    </div>
  );
};

export default Table;
