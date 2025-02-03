import { useState, useEffect } from "react"
import FestivalService from "../../services/FestivalService.js"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import FestivalsTable from "./FestivalsTable.jsx"

const AllFestivalsList = () => {

    const [festivals, setFestivals] = useState([])

    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        const fetchAllFestivals = async () => {
            try {
                const response = await FestivalService.getAllFestivals();
                setFestivals(response.data.festivals)
                setErrorMessage("")
            } catch (e) {
                setErrorMessage(e.message)
                
            }
        }
        fetchAllFestivals()
    }, [])


    return (<>
        <div>List of all festivals</div>
        <ErrorMessage message={errorMessage} />
        <FestivalsTable festivals={festivals} />
    </>)
}

export default AllFestivalsList