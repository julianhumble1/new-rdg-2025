import Table from "../common/Table/Table.jsx";
import { getPeopleColumns } from "../common/Table/columns/people.columns.jsx";

const PeopleTable = ({ people, responseType = "PUBLIC" }) => {
  return <Table data={people} columns={getPeopleColumns(responseType)} />;
};

export default PeopleTable;
