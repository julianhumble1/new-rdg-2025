import axios from "axios"
import Cookies from "js-cookie"

export default class FestivalService {

    static createNewFestival = async (name, venueId, year, month, description) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/festivals",
                {
                    "name": name.trim(),
                    venueId: venueId,
                    year: year,
                    month: month,
                    description: description.trim()
                }, {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }

            )
            return response;
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 400) {
                throw new Error("Bad request: details are not in expected format")
            } else if (e.response.status === 404) {
                throw new Error("No Venue with this id.")
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

    static getFestivalById = async (festivalId) => {
    
        try {
            const response = await axios.get(`http://localhost:8080/festivals/${festivalId}`)
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 404) {
                throw new Error("No Festival with this id.")
            }
        }
    }

    static deleteFestivalById = async (festivalId) => {
    
        const token = Cookies.get("token")

        try {
            const response = await axios.delete(`http://localhost:8080/festivals/${festivalId}`,{
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
            })
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 404) {
                throw new Error("No Festival with this id.")
            }
        }
            
    }

    static updateFestival = async (festivalId, name, venueId, year, month, description) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.patch(`http://localhost:8080/festivals/${festivalId}`,
                {
                    "name": name.trim(),
                    venueId: venueId,
                    year: year,
                    month: month,
                    description: description.trim()
                }, {
                    headers: {
                        "Authorization" : `Bearer ${token}`
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
                throw new Error("No Festival/Venue with this id.")
            } 
        }

    }

}