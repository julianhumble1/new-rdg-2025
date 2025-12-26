import DataTable from "react-data-table-component";
import { customStyles } from "./styles.js";

const Table = ({ data, columns }) => {
  return (
    <div className="rounded-xl shadow-xl p-2 min-h-screen z-0">
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
