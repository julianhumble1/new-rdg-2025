import { usersColumns } from "../common/Table/columns/users.columns.jsx";
import Table from "../common/Table/Table.jsx";

const UsersTable = ({ users }) => {
  return <Table data={users} columns={usersColumns} />;
};

export default UsersTable;
