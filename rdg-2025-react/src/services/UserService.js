import axios from "axios"
import Cookies from "js-cookie"

export default class UserService {

    static login = async (username, password) => {
        try {
            const response = await axios.post("http://localhost:8080/auth/signin", { "username": username, "password": password })
            return response.data
        } catch (error) {
            throw new Error(error.message)
        }
    }

    static checkUser = async () => {
        try {
            const token = Cookies.get("token")
            const response = await axios.get("http://localhost:8080/checkAuth/user", {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            })
            return response;
        } catch (error) {
            throw new Error(error)
        }
    }

    static checkAdmin = async () => {
        try {
            const token = Cookies.get("token")
            const response = await axios.get("http://localhost:8080/checkAuth/admin", {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            })
            return response;
        } catch (error) {
            throw new Error(error)
        }
    }

}