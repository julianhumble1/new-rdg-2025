import { getAwardsColumns } from "../common/Table/columns/awards/awards.columns.jsx";
import Table from "../common/Table/Table.jsx";

const AwardsTable = ({ awards, handleDelete }) => {
  if (awards.length > 0) {
    return <Table data={awards} columns={getAwardsColumns()} />
  };
}
export default AwardsTable;
