import axios from "axios"
import Cookies from "js-cookie"

export default class AwardService {

    static createNewAward = async (name, productionId, personId, festivalId) => {
        const token = Cookies.get("token")

        try {
            
            const response = await axios.post("http://localhost:8080/awards",
                {
                    name: name.trim(),
                    productionId,
                    personId,
                    festivalId
            }, {
                headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 404) {
                throw new Error("This production, person or festival does not exist")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }
    }

    static getAwardById = async (awardId) => {
        try {
            const response = await axios.get(`http://localhost:8080/awards/${awardId}`)
            return response
        } catch (e) {
            if (e.response.status === 404) {
                throw new Error("This award does not exist")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }
    }

    static updateAward = async (awardId, name, productionId, personId, festivalId) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.patch(`http://localhost:8080/awards/${awardId}`,
                {
                    name: name.trim(),
                    productionId,
                    personId,
                    festivalId
            }, {
                headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator")
            } else if (e.response.status === 404) {
                throw new Error("Does not exist")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }
    }

    static deleteAward = async (awardId) => {
        const token = Cookies.get("token")

        
        try {
            const response = await axios.delete(`http://localhost:8080/awards/${awardId}`,
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
            } else if (e.response.status === 404) {
                throw new Error("Does not exist")
            } else if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else {
                throw new Error(e.message)
            }
        }
    }
}