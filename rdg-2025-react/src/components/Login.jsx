import { Label, TextInput, Button } from "flowbite-react"
import { useState } from "react";
import { useNavigate } from "react-router-dom";
import Cookies from "js-cookie";
import UserService from "../services/UserService.js";
import RoleHelper from "../utils/RoleHelper.js";
import ErrorMessage from "./modals/ErrorMessage.jsx";

const AltLogin = ({ loggedIn, setLoggedIn }) => {

    const [username, setUsername] = useState("");
    const [password, setPassword] = useState("");

    const [errorMessage, setErrorMessage] = useState("")

    const navigate = useNavigate()

    const handleLogin = async (event) => {
        event.preventDefault();
        try {
            const responseData = await UserService.login(username, password)
            const mainRole = RoleHelper.determineMainRole(responseData.roles)
            Cookies.set("token", responseData.token, { expires: 7 })
            Cookies.set("role", mainRole, { expires: 7 })
            setLoggedIn(true)
            navigate("/dashboard")

        } catch (error) {
            setErrorMessage("Username or password incorrect. Please try again.")
        }
    }


    return (
        <div className="flex justify-center"> 
            <form
                onSubmit={(event) => handleLogin(event)}
                className="flex lg:w-1/2 w-full flex-col gap-4 min-w-1/2 border-4 p-4 m-4 rounded-xl border-sky-900 border-opacity-60">
                <div className="text-2xl font-bold">
                    Login
                </div>
                <div>
                    <div className="mb-2">
                        <Label value="Username" />
                    </div>
                    <TextInput placeholder="Username" required onChange={(e) => setUsername(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2">
                        <Label value="Password" />
                    </div>
                    <TextInput placeholder="Password" type="password" required onChange={(e) => setPassword(e.target.value)}/>
                </div>
                <button type="submit" className="bg-green-700 hover:bg-green-800 text-white p-2 rounded transition">Login</button>
                <ErrorMessage message={errorMessage} />
            </form>

        </div>
    )
}

export default AltLogin