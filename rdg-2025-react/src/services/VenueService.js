import axios from "axios"
import Cookies from "js-cookie"

export default class VenueService {

    static createNewVenue = async (name, address, town, postcode, notes, url) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/venues/new", 
                {
                    "name": name,
                    "address": address,
                    "town": town,
                    "postcode": postcode,
                    "notes": notes,
                    "url": url
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
                throw new Error("Venue with this name already exists")
            } else if (e.response.status === 400) {
                throw new Error("Bad request: details are not in expected format")
            }
        }
    }

    static getAllVenues = async () => {
        
        const token = Cookies.get("token")

        try {
            const response = await axios.get("http://localhost:8080/venues/",
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
            }
        }

    }

}