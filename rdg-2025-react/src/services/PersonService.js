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


}