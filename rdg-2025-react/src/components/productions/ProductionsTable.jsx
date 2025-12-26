import Table from "../common/Table/Table.jsx";
import { productionsColumns } from "../common/Table/columns/productions.columns.jsx";

const ProductionsTable = ({ productions }) => {
  return <Table data={productions} columns={productionsColumns} />;
};

export default ProductionsTable;
