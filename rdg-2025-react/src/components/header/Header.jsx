import { Link, useNavigate } from "react-router-dom"
import Cookies from "js-cookie";
import { useState, useEffect } from "react";
import NavButton from "./NavButton.jsx";

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
        <div className="w-full h-16 bg-sky-900 flex flex-row gap-3 p-3 my-auto justify-between">
            <div className="flex flex-row gap-3">
                <img src="/src/assets/new_logo_transparent.png" alt="rdg-logo" />
                <div className="my-auto">
                    <NavButton buttonName={"Home"} />
                </div>
                {loggedIn && 
                    <div className="my-auto">
                        <NavButton buttonName={"Dashboard"}  />
                    </div>
                }
            </div>
                
            {loggedIn &&
                <div className="flex flex-row gap-3">
                    <div className="my-auto text-white">Logged in as {role} </div>
                    <button onClick={handleLogout} className="text-blue-500 underline hover:text-blue-800">Logout</button>
                </div>
            }
            {!loggedIn &&
                <div className="my-auto">
                    <Link to="/login" className=" text-blue-500 underline hover:text-blue-800">Login</Link>
                </div>
            }
        </div>
        
  </>)
}

export default Header