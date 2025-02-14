import { useState, useCallback, useEffect } from "react";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import FestivalService from "../../services/FestivalService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import FestivalHighlight from "./FestivalHighlight.jsx";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import EditFestivalForm from "./EditFestivalForm.jsx";

const FestivalPage = () => {

    const festivalId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate()

    const [festivalData, setFestivalData] = useState(null)

    const [performances, setPerformances] = useState([])

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false)

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

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
            await FestivalService.deleteFestivalById(festivalData.id)
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

    return (
        <div>
            {showConfirmDelete &&
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={festivalData} handleConfirmDelete={ handleConfirmDelete } />
            }
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />

            <div className="flex w-full justify-center md:my-2">
                {(festivalData && !editMode && performances.length > 0) &&
                    <div className="grid md:grid-cols-5 grid-cols-1 w-full lg:w-1/2 md:w-2/3 md:shadow-md min-h-[26rem]">
                        <FestivalHighlight festivalData={festivalData} setEditMode={setEditMode} handleDelete={handleDelete} />
                        <PerformancesTable performances={performances}/>
                    </div>
                }

                {(festivalData && !editMode && performances.length === 0) &&
                    <div className="grid md:grid-cols-3 grid-cols-1 w-full lg:w-1/2 md:w-2/3 md:shadow-md h-[26rem]">
                        <FestivalHighlight festivalData={festivalData} setEditMode={setEditMode} handleDelete={handleDelete} />
                    </div>
                }
                {(festivalData && editMode) &&
                    <EditFestivalForm festivalData={festivalData} handleEdit={handleEdit} setEditMode={setEditMode}/>
                }
            </div>
        </div>
    )
}

export default FestivalPage