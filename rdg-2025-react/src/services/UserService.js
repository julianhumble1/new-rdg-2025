import axios from "axios"
import Cookies from "js-cookie"

export default class UserService {

    static login = async (username, password) => {
        try {
            const response = await axios.post("http://localhost:8080/api/auth/signin", { "username": username, "password": password })
            return response.data
        } catch (error) {
            throw new Error(error.message)
        }
    }

    static checkMod = async () => {
        try {
            const token = Cookies.get("token")
            const response = await axios.get("http://localhost:8080/api/test/mod", {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            })
            console.log(response)
            return true;
        } catch (error) {
            console.log(error)
            throw new Error(error.message)
        }
    }

    static checkUser = async () => {
        try {
            const token = Cookies.get("token")
            const response = await axios.get("http://localhost:8080/api/test/user", {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            })
            console.log(response)
            return true;
        } catch (error) {
            console.log(error)
            throw new Error(error.message)
        }
    }

    static checkAdmin = async () => {
        try {
            const token = Cookies.get("token")
            const response = await axios.get("http://localhost:8080/api/test/admin", {
                headers: {
                    "Authorization": `Bearer ${token}`
                }
            })
            console.log(response)
            return true;
        } catch (error) {
            console.log(error)
            throw new Error(error.message)
        }
    }

}