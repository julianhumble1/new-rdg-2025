import { Link, useNavigate } from "react-router-dom"
import Cookies from "js-cookie";
import { useState, useEffect } from "react";

const Header = ({ loggedIn, setLoggedIn }) => {
    
    const navigate = useNavigate()

    const [role, setRole] = useState("")

    useEffect(() => {
        const roleFromCookies = Cookies.get("role")
        if (roleFromCookies) setRole(roleFromCookies)
    }, [loggedIn])
    
    const handleLogout = () => {
        Cookies.remove("token")
        Cookies.remove("role")
        setLoggedIn(false)
        navigate("/login")
    }
    
    
    return (<>
        <div className="w-full h-10 bg-yellow-300 flex flex-row gap-3">
            {loggedIn && <>
                <div className="my-auto">Logged in as {role} </div>
                <button onClick={handleLogout} className="text-blue-500 underline hover:text-blue-800">Logout</button>
            </>}
            {!loggedIn &&
                <div>
                    <Link to="/login" className="text-blue-500 underline hover:text-blue-800">Login</Link>
                </div>
            }
        </div>
        
  </>)
}

export default Header