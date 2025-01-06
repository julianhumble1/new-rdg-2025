import { useEffect } from "react"
import UserService from "../../services/UserService.js";
import { useNavigate } from "react-router-dom";

const AdminDashboard = () => {

  const navigate = useNavigate();

  useEffect(() => {
      const checkAdmin = async () => {
        try {
          await UserService.checkAdmin()
        } catch (error) {
          console.log(error)
          navigate("/dashboard")
        }
  
      }
      checkAdmin()
    })

  return (
    <div>Admin Dashboard</div>
  )
}

export default AdminDashboard