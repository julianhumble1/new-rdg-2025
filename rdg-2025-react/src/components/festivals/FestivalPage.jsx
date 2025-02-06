import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"
import FestivalService from "../../services/FestivalService.js";
import MonthDateUtils from "../../utils/MonthDateUtils.js";
import { format } from "date-fns";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";

const FestivalPage = () => {

    const festivalId = useParams().id;
    const navigate = useNavigate()

    const [festivalData, setFestivalData] = useState(null)

    const [performances, setPerformances] = useState([])

    const [errorMessage, setErrorMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false)

    const getFestivalData = useCallback( async () => {
        try {
            const response = await FestivalService.getFestivalById(festivalId)
            setFestivalData(response.data.festival)
            setPerformances(response.data.performances)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [festivalId])

    useEffect(() => {
        getFestivalData()
    }, [getFestivalData])

    const handleDelete = () => {
        setShowConfirmDelete(true)
    }

    const handleConfirmDelete = async () => {
        try {
            const response = await FestivalService.deleteFestivalById(festivalData.id)
            navigate("/festivals")
        } catch (e) {
            setErrorMessage(e.message)
        }
        setShowConfirmDelete(false)
    }


    return (<>
        
        <div className="font-bold text-xl p-3">Festival {festivalId}: </div>
    
        <ErrorMessage message={errorMessage} />

        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={festivalData} handleConfirmDelete={ handleConfirmDelete } />
        }

        {festivalData &&
            <div className="flex flex-row bg-gray-300 m-2 p-2 rounded w-1/2 justify-between">
                <div className="flex flex-col w-full">
                    <div className="flex flex-row gap-2">
                        <div className="font-bold">{festivalData.name}</div>
                        <div className="italic ">{festivalData.month ? MonthDateUtils.monthMapping[festivalData.month] : ""} {festivalData.year}</div>
                    </div>
                    <div>Venue: {festivalData.venue &&
                        <Link className="text-blue-500 hover:text-blue-700 hover:underline" to={`/venues/${festivalData.venue.id}`}>
                            {festivalData.venue.name}
                        </Link>
                        }
                    </div>
                    <div>Description: {festivalData.description} </div>
                    <div>Created: {format(new Date(festivalData.createdAt), "dd-MM-yyyy")} </div>
                    <div>Updated: {format(new Date(festivalData.updatedAt), "dd-MM-yyyy")} </div>
                </div>
                <div>
                    <div className="flex flex-row">
                        <button
                            className="text-blue-500 hover:text-blue-700 hover:underline"
                            onClick={handleDelete}>
                            Delete
                        </button>
                    </div>
                </div>
            </div >
        }

        <PerformancesTable performances={performances} />
    
    </>)
}

export default FestivalPage