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
        <div className="w-full h-10 bg-blue-700 flex flex-row">
            {loggedIn && <>
                <div>Logged in as {role} </div>
                <button onClick={handleLogout}>Logout</button>
            </>}
            {!loggedIn &&
                <div>
                    <Link to="/login" className="text-blue-500 underline">Login</Link>
                </div>
            }
        </div>
        
  </>)
}

export default Header