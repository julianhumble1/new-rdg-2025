import { useEffect } from "react";
import { useNavigate } from "react-router-dom";
import UserService from "../../services/UserService.js";

const ModDashboard = () => {

  const navigate = useNavigate()

  useEffect(() => {
    const checkMod = async () => {
      try {
        await UserService.checkMod()
      } catch (error) {
        console.log(error)
        navigate("/dashboard")
      }

    }
    checkMod()
  })

  return (
    <div>Mod Dashboard</div>
  )
}

export default ModDashboard