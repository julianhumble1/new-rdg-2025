import { useState, useCallback, useEffect } from "react";
import { useParams, useSearchParams, useNavigate, Link } from "react-router-dom";
import ProductionService from "../../services/ProductionService.js";
import VenueService from "../../services/VenueService.js";
import ProductionsTable from "../productions/ProductionsTable.jsx";
import FestivalsTable from "../festivals/FestivalsTable.jsx";
import VenueHighlight from "./VenueHighlight.jsx";
import EditVenueForm from "./EditVenueForm.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";

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
            // check whether item to delete is a production or venue
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


    return (
        <div>
            {showConfirmDelete &&
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={itemToDelete} handleConfirmDelete={ handleConfirmDelete } />
            }
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            <div className="flex w-full justify-center">
                {(venueData && !editMode) &&
                    <VenueHighlight venueData={venueData} setEditMode={setEditMode} handleDelete={handleDelete} />
                }
                {(venueData && editMode) &&
                    <EditVenueForm venueData={venueData} handleEdit={handleEditVenue} setEditMode={setEditMode} />
                }
            </div>
            <div className="flex flex-col gap-1">
                <ProductionsTable productions={productions} handleDelete={handleDelete} />
                <FestivalsTable festivals={festivals} />
            </div>
            
        </div>
    )
}

export default VenuePage