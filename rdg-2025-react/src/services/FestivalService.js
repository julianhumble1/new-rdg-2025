import axios from "axios"
import Cookies from "js-cookie"

export default class FestivalService {

    static createNewFestival = async (name, venueId, year, month, description) => {
        console.log("venue id: " +  venueId)

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/festivals",
                {
                    "name": name,
                    venueId: venueId,
                    year: year,
                    month: month,
                    description: description
                }, {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }

            )
            console.log(response)
            return response;
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 400) {
                throw new Error("Bad request: details are not in expected format")
            }
        }
    }

    static getAllFestivals = async () => {
        
        const token = Cookies.get("token")

        try {
            const response = await axios.get("http://localhost:8080/festivals",
                {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }

    }

}