import Cookies from "js-cookie"
import axios from "axios"

export default class PersonService {

    static addNewPerson = async (firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/people",
                {
                    firstName: firstName.trim(),
                    lastName: lastName.trim(),
                    summary: summary.trim(),
                    homePhone: homePhone.trim(),
                    mobilePhone: mobilePhone.trim(),
                    addressStreet: addressStreet.trim(),
                    addressTown: addressTown.trim(),
                    addressPostcode: addressPostcode.trim()
                }, {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }

    }

    static getAllPeople = async () => {
        const token = Cookies.get("token")

        try {
            const response = await axios.get("http://localhost:8080/people",
                {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }

            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

    static deletePersonById = async (personId) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.delete(`http://localhost:8080/people/${personId}`,
               {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

    static getPersonById = async (personId) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.get(`http://localhost:8080/people/${personId}`,
               {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

    static updatePerson = async (personId, firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode) => {
        
        const token = Cookies.get("token")

        try {
            const response = await axios.patch(`http://localhost:8080/people/${personId}`,
                {
                    firstName: firstName.trim(),
                    lastName: lastName.trim(),
                    summary: summary.trim(),
                    homePhone: homePhone.trim(),
                    mobilePhone: mobilePhone.trim(),
                    addressStreet: addressStreet.trim(),
                    addressTown: addressTown.trim(),
                    addressPostcode: addressPostcode.trim()
                }, {
                    headers: {
                        "Authorization": `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

}