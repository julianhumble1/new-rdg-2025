import { Link, useNavigate } from "react-router-dom"
import { useState } from "react";
import Cookies from "js-cookie";
import UserService from "../services/UserService.js";
import RoleHelper from "../utils/RoleHelper.js";

const Login = ({ loggedIn, setLoggedIn}) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [failedLoginAttempt, setFailedLoginAttempt] = useState(false)

    const navigate = useNavigate()

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const responseData = await UserService.login(username, password)
            console.log(responseData)
            const mainRole = RoleHelper.determineMainRole(responseData.roles)
            Cookies.set("token", responseData.token, { expires: 7 })
            Cookies.set("role", mainRole, { expires: 7 })
            setLoggedIn(true)

            navigate("/dashboard")

        } catch (error) {
            console.log(error)
            setFailedLoginAttempt(true)
        }
    }

    return (<>
    
        <div>
        <Link to="/home">
            Home
        </Link>
        </div>

        {!loggedIn &&
            <form onSubmit={handleLogin}>
                <div className="flex flex-col gap-3">

                    <input type="text" placeholder="username@username.com" className="my-3 p-2 border border-black" value={username} onChange={(e) => setUsername(e.target.value)} />
                    <input type="password" placeholder="Password" className="my-3 p-2 border border-black" value={password} onChange={(e) => setPassword(e.target.value)}/>

                </div>
                {failedLoginAttempt &&
                    <div className="text-red-600">
                        Username or password incorrect. Please try again.
                    </div>
                }
                <button type="submit" className="bg-green-300 px-3 py-1">
                    Login
                </button>
            </form>
        }
        {loggedIn &&
            <div>
                <div>You are already logged in</div>
                <button className="text-blue-500 underline hover:text-blue-800">Logout</button>
            </div>
        }



    </>)
}

export default Login