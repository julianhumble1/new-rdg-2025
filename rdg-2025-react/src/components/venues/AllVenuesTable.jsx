import Table from "../common/Table/Table.jsx";
import { venueColumns } from "../common/Table/columns/venues.columns.jsx";

const AllVenuesTable = ({ venues }) => {
  return <Table data={venues} columns={venueColumns} />;
};

export default AllVenuesTable;
