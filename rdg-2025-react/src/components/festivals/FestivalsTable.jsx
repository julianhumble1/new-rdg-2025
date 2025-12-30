import Table from "../common/Table/Table.jsx";
import { festivalColumns } from "../common/Table/columns/festivals.columns.jsx";

const FestivalsTable = ({ festivals }) => {
  return <Table data={festivals} columns={festivalColumns} />;
};

export default FestivalsTable;
