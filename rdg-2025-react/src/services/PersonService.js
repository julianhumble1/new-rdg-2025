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
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 409) {
                throw new Error("Person with this name already exists.")
            } else if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator.")
            } else if (e.response.status === 400) {
                throw new Error("Bad Request: Details not in expected format.")
            }
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
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            }
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
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator.")
            } else if (e.response.status === 404) {
                throw new Error("No Person with this id.")
            } 
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
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 404) {
                throw new Error("No Person with this id.")
            } else if (e.response.status === 400) {
                throw new Error("Bad Request: Details not in expected format.")
            }
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
            if (e.response.status === 500) {
                throw new Error("Internal server error")
            } else if (e.response.status === 409) {
                throw new Error("Person with this name already exists.")
            } else if (e.response.status === 404) {
                throw new Error("No Person with this id.")
            } else if (e.response.status === 400) {
                throw new Error("Bad Request: Details not in expected format.")
            } else if (e.response.status === 401 || e.response.status === 403) {
                throw new Error("Failed to authenticate as administrator.")
            }
        }
    }

}