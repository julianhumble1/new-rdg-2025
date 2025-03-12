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


}