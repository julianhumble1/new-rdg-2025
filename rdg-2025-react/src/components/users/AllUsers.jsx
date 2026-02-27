import { useQuery } from "@tanstack/react-query";
import CustomSpinner from "../common/CustomSpinner.jsx";
import UsersTable from "./UsersTable.jsx";
import UserService from "../../services/UserService.js";

const AllUsers = () => {
  const { data: users, isLoading } = useQuery({
    queryKey: ["users"],
    queryFn: async () => {
      const response = await UserService.getAllUsers();
      return response.data.users;
    },
  });

  if (isLoading) return <CustomSpinner />;

  return <UsersTable users={users} />;
};

export default AllUsers;
