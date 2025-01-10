import { useEffect } from "react"
import UserService from "../../services/UserService.js";
import { useNavigate, Link } from "react-router-dom";

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

  return (<>
    <div>Admin Dashboard</div>
    <div className="flex flex-col gap-2 border border-black w-fit p-3">
      <div className="border border-yellow-300 w-fit p-1">
        <div className="italic">
          Venues
        </div>
        <div className="flex flex-col">
          <Link to="/venues/new" className="underline text-blue-500 hover:text-blue-700">Add New Venue</Link>
          <Link to="/venues/" className="underline text-blue-500 hover:text-blue-700">See All Venues</Link>
        </div>
      </div>
    </div>
  </>)
}

export default AdminDashboard