import axios from "axios"
import Cookies from "js-cookie"

export default class ProductionService {

    static createNewProduction = async (name, venueId, author, description, auditionDate, sundowners, notConfirmed, flyerFile) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/productions", 
                {
                    name: name,
                    venueId: venueId,
                    author: author,
                    description: description,
                    auditionDate: auditionDate,
                    sundowners: sundowners,
                    notConfirmed: notConfirmed,
                    flyerFile: flyerFile
                }, {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 409) {
                throw new Error("Production with this name already exists")
            } else if (e.response.status === 400) {
                throw new Error("Bad request: details are not in expected format")
            }
        }
    }

    static getAllProductions = async () => {
        try {
            const response = await axios.get("http://localhost:8080/productions")
            return response
        } catch (e) {
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }
    }

}