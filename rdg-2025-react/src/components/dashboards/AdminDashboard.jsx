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
          navigate("/dashboard")
        }
  
      }
      checkAdmin()
    })

  return (<>
    <div className="p-3 font-bold">Admin Dashboard</div>
    <div className="flex gap-2 pl-2">
      <div className="flex flex-col gap-2 border border-black w-fit p-3">
        <div className=" w-fit p-1">
          <div className="italic underline">
            Venues
          </div>
          <div className="flex flex-col">
            <Link to="/venues/new" className="underline text-blue-500 hover:text-blue-700">Add New Venue</Link>
            <Link to="/venues" className="underline text-blue-500 hover:text-blue-700">See All Venues</Link>
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-2 border border-black w-fit p-3">
        <div className=" w-fit p-1">
          <div className="italic underline">
            Productions
          </div>
          <div className="flex flex-col">
            <Link to="/productions/new" className="underline text-blue-500 hover:text-blue-700">Add New Production</Link>
            <Link to="/productions" className="underline text-blue-500 hover:text-blue-700">See All Productions</Link>
          </div>
        </div>
        
      </div>

      <div className="flex flex-col gap-2 border border-black w-fit p-3">
        <div className=" w-fit p-1">
          <div className="italic underline">
            Festivals
          </div>
          <div className="flex flex-col">
            <Link to="/festivals/new" className="underline text-blue-500 hover:text-blue-700">Add New Festival</Link>
            <Link to="/festivals" className="underline text-blue-500 hover:text-blue-700">See All Festivals</Link>
          </div>
        </div>
      </div>

      <div className="flex flex-col gap-2 border border-black w-fit p-3">
        <div className=" w-fit p-1">
          <div className="italic underline">
            Performances
          </div>
          <div className="flex flex-col">
            <Link to="/performances/new" className="underline text-blue-500 hover:text-blue-700">Add New Performance</Link>
          </div>
        </div>
      </div>
      
    </div>
  </>)
}

export default AdminDashboard