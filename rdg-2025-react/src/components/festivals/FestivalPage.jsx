import { useCallback, useEffect, useState } from "react"
import { Link, useNavigate, useParams } from "react-router-dom"
import FestivalService from "../../services/FestivalService.js";
import MonthDateUtils from "../../utils/MonthDateUtils.js";
import { format } from "date-fns";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import EditFestivalForm from "./EditFestivalForm.jsx";

const FestivalPage = () => {

    const festivalId = useParams().id;
    const navigate = useNavigate()

    const [festivalData, setFestivalData] = useState(null)

    const [performances, setPerformances] = useState([])

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false)

    const [editMode, setEditMode] = useState(false)

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

    const handleEdit = async (event, festivalId, name, venueId, year, month, description) => {
        event.preventDefault()
        try {
            const response = await FestivalService.updateFestival(festivalId, name, venueId, year, month, description)
            setEditMode(false)
            setSuccessMessage("Successfully edited")
            getFestivalData()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }


    return (<>
        
        <div className="font-bold text-xl p-3">Festival {festivalId}: </div>
    
        <SuccessMessage message={successMessage} />
        <ErrorMessage message={errorMessage} />

        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={festivalData} handleConfirmDelete={ handleConfirmDelete } />
        }

        {(festivalData && !editMode) &&
            <div className="flex flex-row bg-gray-300 m-2 p-2 rounded w-1/2 justify-between">
                <div className="flex flex-col w-full">
                    <div className="font-bold text-lg">{festivalData.name}</div>
                    <div>Venue: {festivalData.venue &&
                        <Link className="text-blue-500 hover:text-blue-700 hover:underline" to={`/venues/${festivalData.venue.id}`}>
                            {festivalData.venue.name}
                        </Link>
                        }
                    </div>
                    <div>Date: {festivalData.month ? MonthDateUtils.monthMapping[festivalData.month] : ""} {festivalData.year}</div>
                    <div>Description: {festivalData.description} </div>
                    <div>Created: {format(new Date(festivalData.createdAt), "dd-MM-yyyy")} </div>
                    <div>Updated: {format(new Date(festivalData.updatedAt), "dd-MM-yyyy")} </div>
                </div>
                <div>
                    <div className="flex flex-row gap-2">
                        <button
                            className="text-blue-500 hover:text-blue-700 hover:underline"
                            onClick={() => setEditMode(true)}>
                            Edit
                        </button>
                        <button
                            className="text-blue-500 hover:text-blue-700 hover:underline"
                            onClick={handleDelete}>
                            Delete
                        </button>
                    </div>
                </div>
            </div >
        }

        {(festivalData && editMode) &&
            <div className="flex flex-row bg-gray-300 m-2 p-2 rounded w-1/2 justify-between">
                <EditFestivalForm festivalData={festivalData} handleEdit={handleEdit} />
                <button className="text-blue-500 hover:text-blue-700 hover:underline"
                            onClick={() => setEditMode(false)}>
                    Cancel edit
                </button>
            </div>
        }

        <PerformancesTable performances={performances} />
    
    </>)
}

export default FestivalPage