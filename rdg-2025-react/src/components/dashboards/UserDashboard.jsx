import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService.js";

const UserDashboard = () => {
  const navigate = useNavigate();

  useEffect(() => {
    const checkUser = async () => {
      try {
        await UserService.checkUser();
      } catch (error) {
        console.log(error);
        navigate("/dashboard");
      }
    };
    checkUser();
  });

  return <div>User Dashboard</div>;
};

export default UserDashboard;
