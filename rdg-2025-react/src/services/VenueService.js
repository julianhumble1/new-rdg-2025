import axios from "axios"
import Cookies from "js-cookie"

export default class VenueService {

    static createNewVenue = async (name, address, town, postcode, notes, url) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/venues", 
                {
                    "name": name.trim(),
                    "address": address.trim(),
                    "town": town.trim(),
                    "postcode": postcode.trim(),
                    "notes": notes.trim(),
                    "url": url.trim()
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
            const response = await axios.get("http://localhost:8080/venues",
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

    static deleteVenue = async (venueId) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.delete(`http://localhost:8080/venues/${venueId}`,
                {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response;
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 404) {
                throw new Error("Venue does not exist to be deleted")
            } else if (e.response.status === 400) {
                throw new Error("Provided venue id must be integer")
            }
        }
    }

    static getVenueById = async (venueId) => {

        try {
            const response = await axios.get(`http://localhost:8080/venues/${venueId}`)
            return response
        } catch (e) {
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 404) {
                throw new Error("Venue does not exist")
            } else if (e.response.status === 400) {
                throw new Error("Provided venue id must be integer")
            }
        }

    }

    static updateVenue = async (venueId, name, address, town, postcode, notes, url) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.patch(`http://localhost:8080/venues/${venueId}`, 
                {
                    "name": name.trim(),
                    "address": address.trim(),
                    "town": town.trim(),
                    "postcode": postcode.trim(),
                    "notes": notes.trim(),
                    "url": url.trim()
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
            } else if (e.response.status === 404) {
                throw new Error("Can not find venue with this id")
            }
        }
    }

}