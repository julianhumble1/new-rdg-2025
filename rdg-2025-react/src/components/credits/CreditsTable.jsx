import { creditsColumns } from "../common/Table/columns/credits.columns.jsx";
import Table from "../common/Table/Table.jsx";

const CreditsTable = ({ credits, handleDelete }) => {
  if (credits.length > 0) {
    return <Table data={credits} columns={creditsColumns} />;
  }
};

export default CreditsTable;
