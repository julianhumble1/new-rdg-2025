import axios from "axios"
import Cookies from "js-cookie"

export default class PerformanceService {

    static addNewPerformance = async (productionId, venueId, festivalId, performanceTime, description, standardPrice, concessionPrice, boxOffice) => {

        const token = Cookies.get("token")

        try {
            const response = await axios.post("http://localhost:8080/performances",
                {
                    productionId: productionId,
                    venueId: venueId,
                    festivalId: festivalId,
                    time: performanceTime,
                    description: description.trim(),
                    standardPrice: standardPrice.trim(),
                    concessionPrice: concessionPrice.trim(),
                    boxOffice: boxOffice.trim()
                }, {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message, e)
        }

    }

    static deletePerformance = async (performanceId) => {
        const token = Cookies.get("token")

        try {
            const response = await axios.delete(`http://localhost:8080/performances/${performanceId}`,
                {
                    headers: {
                        "Authorization" : `Bearer ${token}`
                    }
                }
            )
            return response
        } catch (e) {
            throw new Error(e.message)
        }
    }

}