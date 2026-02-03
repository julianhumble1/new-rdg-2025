import {
  getCreditsColumns,
} from "../common/Table/columns/credits/credits.columns.jsx";
import Table from "../common/Table/Table.jsx";

const CreditsTable = ({ credits, handleDelete }) => {
  if (credits.length > 0) {
    return <Table data={credits} columns={getCreditsColumns()} />;
  }
};

export default CreditsTable;
