import { useNavigate, useParams, useSearchParams } from "react-router-dom"
import { useState, useEffect, useCallback } from "react";
import VenueService from "../../services/VenueService.js";
import EditVenueForm from "./EditVenueForm.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import { format } from "date-fns";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import FestivalsTable from "../festivals/FestivalsTable.jsx"
import ProductionService from "../../services/ProductionService.js";
import ProductionsTable from "../productions/ProductionsTable.jsx";

const VenuePage = () => {

    const venueId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [venueData, setVenueData] = useState(null);
    const [productions, setProductions] = useState([]);
    const [festivals, setFestivals] = useState([])

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [showConfirmDelete, setShowConfirmDelete] = useState(false);
    const [itemToDelete, setItemToDelete] = useState(null)

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const fetchVenueData = useCallback(async () => {
        try {
            const response = await VenueService.getVenueById(venueId)
            setVenueData(response.data.venue)
            setProductions(response.data.productions)
            setFestivals(response.data.festivals)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [venueId])

    useEffect(() => {
        fetchVenueData()
    }, [fetchVenueData]) 

    const handleDelete = (item) => {
        setItemToDelete(item)
        setShowConfirmDelete(true)
    }

    const handleConfirmDelete = async (item) => {
        try {
            if (item.postcode == null) {
                const response = await ProductionService.deleteProduction(item.id)
                fetchVenueData()
            } else {
                await VenueService.deleteVenue(item.id)
                navigate("/venues")
            }
            setShowConfirmDelete(false)
            setSuccessMessage(`Successfully deleted '${item.name}'`)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }


    const handleEditVenue = async (event, id, name, address, town, postcode, notes, url) => {
        event.preventDefault()
        try {
            const response = await VenueService.updateVenue(id, name, address, town, postcode, notes, url)
            setSuccessMessage("Successfully edited!")
            setErrorMessage("")
            fetchVenueData()
            setEditMode(false)
        } catch (e) {
            setSuccessMessage("")
            setErrorMessage(e.message)
        }
    }

    return (<>
        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={itemToDelete} handleConfirmDelete={ handleConfirmDelete } />
        }

        <div className="font-bold text-xl p-3 ">
            Venue {venueId}: 
        </div>
        
        <SuccessMessage message={successMessage} />
        <ErrorMessage message={errorMessage} />

        {(venueData && !editMode) &&
            <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                <div>
                    <div className="font-bold"> {venueData.name}</div>
                    <div>Address: {venueData.address}</div>
                    <div>Town/City: {venueData.town}</div>
                    <div>Postcode: {venueData.postcode}</div>
                    <div>Notes: {venueData.notes}</div>
                    <div>URL: {venueData.url}</div>
                    <div>Created: {format(new Date(venueData.createdAt), "dd-MM-yyyy")}</div>
                    <div>Updated: {format(new Date(venueData.updatedAt), "dd-MM-yyyy")}</div>
                </div>
                <div className="flex flex-row gap-3 items-start ">
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => handleDelete(venueData)}>
                        Delete
                    </button>
                </div>
            </div>
        }

        {(venueData && editMode) && 
            <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                <div>
                    <EditVenueForm venueData={venueData} handleEdit={handleEditVenue} />
                </div>
                
                <div className="flex flex-row gap-3 items-start ">
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(false)}>
                        Cancel Edit Mode
                    </button>
                </div>
            </div>
        }

        <ProductionsTable productions={productions} handleDelete={handleDelete} />

        <FestivalsTable festivals={festivals} />
    </>)
}

export default VenuePage