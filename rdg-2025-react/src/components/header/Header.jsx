import {  Navbar, NavbarLink, NavbarBrand, NavbarToggle, NavbarCollapse} from "flowbite-react"
import { useState, useEffect } from "react"
import { useNavigate } from "react-router-dom"
import Cookies from "js-cookie"

const AltHeader = ({loggedIn, setLoggedIn}) => {

    const url = window.location.pathname

    const navigate = useNavigate()

    const [role, setRole] = useState("")

    const [showLogin, setShowLogin] = useState(true)

    useEffect(() => {
        const roleFromCookies = Cookies.get("role")
        if (roleFromCookies) setRole(roleFromCookies.substring(5))
        setShowLogin(!loggedIn)
    }, [loggedIn])
    
    const handleLogout = () => {
        Cookies.remove("token")
        Cookies.remove("role")
        setLoggedIn(false)
        navigate("/login")
    }



    return (
        <Navbar fluid rounded className="bg-sky-900 mt-3">
            <NavbarBrand href="/home">
                <img src="/src/assets/new_logo_transparent.png" className="mr-3 h-6 sm:h-9" alt="Flowbite React Logo" />
                <span className="self-center whitespace-nowrap text-xl font-semibold text-white">Runnymede Drama Group</span>
            </NavbarBrand>
            <NavbarToggle className="text-white"/>
            <NavbarCollapse>
                <NavbarLink href="/home" className="text-white" active={url.includes("home")}> Home </NavbarLink>
                {loggedIn &&
                    <NavbarLink href="/dashboard" className="text-white" active={url.includes("dashboard")} >Dashboard</NavbarLink>
                }
                {!loggedIn ? 
                    <NavbarLink href="/login" className="text-white" active={url.includes("login")} >Login</NavbarLink>
                    :
                    <NavbarLink className="text-white hover:cursor-pointer" onClick={handleLogout} >Logout</NavbarLink>
                }
            </NavbarCollapse>
      </Navbar>
    ) 
}

export default AltHeader