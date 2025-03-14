import axios from "axios"
import Cookies from "js-cookie"

export default class CreditService {

    static addNewCredit = async (name, type, productionId, personId, summary) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/credits",
                {
                    name: name.trim(),
                    type: type,
                    productionId: productionId,
                    personId: personId,
                    summary: summary.trim()
                }, {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response;
        } catch (e) {
            if (e.response.status === 500) {
                throw new Error("Internal Server Error.")
            } else if (e.response.status === 404) {
                throw new Error("Production/Person does not exist.")
            } else if (e.response.status === 400) {
                throw new Error("Bad Request: details not in required format.")
            }
        }

    }

    static getCreditById = async (creditId) => {

        try {
            const response = await axios.get(`http://localhost:8080/credits/${creditId}`)
            return response
        } catch (e) {
            if (e.response.status === 500) {
                throw new Error("Internal Server Error.")
            } else if (e.response.status === 404) {
                throw new Error("Credit does not exist.")
            }
        }


    }

    static updateCredit = async (creditId, name, type, productionId, personId, summary) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.patch(`http://localhost:8080/credits/${creditId}`,
                {
                    name: name.trim(),
                    type: type,
                    productionId: productionId,
                    personId: personId,
                    summary: summary.trim()
                }, {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response;
        } catch (e) {
            if (e.response.status === 500) {
                throw new Error("Internal Server Error.")
            } else if (e.response.status === 404) {
                throw new Error("Credit/Production/Person does not exist.")
            } else if (e.response.status === 400) {
                throw new Error("Bad Request: details not in required format.")
            }
        }
    }

    static deleteCreditById = async (creditId) => {

        const token = Cookies.get("token")
        try {
            const response = await axios.delete(`http://localhost:8080/credits/${creditId}`,{
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
                throw new Error("No Credit with this id.")
            }
        }
    }


}