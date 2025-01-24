import { useNavigate, useParams, useSearchParams } from "react-router-dom"
import { useState, useEffect } from "react";
import VenueService from "../../services/VenueService.js";
import ProductionRow from "../productions/ProductionRow.jsx";
import FestivalRow from "../festivals/FestivalRow.jsx";
import DateHelper from "../../utils/DateHelper.js";
import EditVenueForm from "./EditVenueForm.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";

const VenuePage = () => {

    const venueId = useParams().id;
    const [searchParams] = useSearchParams();

    const navigate = useNavigate();

    const [venueData, setVenueData] = useState(null);
    const [productions, setProductions] = useState([]);
    const [festivals, setFestivals] = useState([])

    const [fetchError, setFetchError] = useState("")

    const [editMode, setEditMode] = useState(searchParams.get("edit"))
    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false);
    const [deleteError, setDeleteError] = useState("")
    const [deleteSuccess, setDeleteSuccess] = useState("")

    const fetchVenueData = async () => {
        try {
            const response = await VenueService.getVenueById(venueId)
            setVenueData(response.data.venue)
            setProductions(response.data.productions)
            setFestivals(response.data.festivals)
        } catch (e) {
            setFetchError(e.message)
        }
    }

    const handleDelete = () => {
        setShowConfirmDelete(true)
    }

    const handleConfirmDelete = async (venue) => {
            try {
                await VenueService.deleteVenue(venue.id)
                setShowConfirmDelete(false)
                setDeleteSuccess(`Successfully deleted '${venue.name}'`)
                navigate("/venues")
            } catch (e) {
                setDeleteError(e.message)
            }
        }

    useEffect(() => {
        fetchVenueData()
    }, [venueId])  

    const handleEdit = async (event, id, name, address, town, postcode, notes, url) => {
        event.preventDefault()
        try {
            const response = await VenueService.updateVenue(id, name, address, town, postcode, notes, url)
            setSuccessMessage("Successfully edited!")
            setFailMessage("")
            fetchVenueData()
            setEditMode(false)
        } catch (e) {
            setSuccessMessage("")
            setFailMessage(e.message)
        }
    }

    return (<>
        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} venueToDelete={venueData} handleConfirmDelete={ handleConfirmDelete } />
        }
        <div className="font-bold text-xl p-3 ">
            Venue {venueId}: 
        </div>
        {fetchError && 
            <div className="text-red-500">{fetchError}</div>
        }

        {(venueData && !editMode) &&
            <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                <div>
                    <div className="font-bold"> {venueData.name}</div>
                    <div>Address: {venueData.address}</div>
                    <div>Town/City: {venueData.town}</div>
                    <div>Postcode: {venueData.postcode}</div>
                    <div>Notes: {venueData.notes}</div>
                    <div>URL: {venueData.url}</div>
                    <div>Created: {DateHelper.formatDatabaseDateForDisplay(venueData.createdAt)}</div>
                    <div>Updated: {DateHelper.formatDatabaseDateForDisplay(venueData.updatedAt)}</div>
                </div>
                {successMessage && 
                    <div className="text-green-500">
                        {successMessage}
                    </div>
                }
                <div className="flex flex-row gap-3 items-start ">
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => handleDelete()}>
                        Delete
                    </button>
                </div>
            </div>
        }

        {(venueData && editMode) && 
            <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                <div>
                    <EditVenueForm venueData={venueData} handleEdit={handleEdit} />
                </div>
                
                {failMessage && 
                    <div className="text-red-500">
                        Failed to edit venue: {failMessage}
                    </div>
                }
                <div className="flex flex-row gap-3 items-start ">
                    <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(false)}>
                        Cancel Edit Mode
                    </button>
                    
                </div>
            </div>
        }


        {productions && 
            <div>
                <h3 className="font-bold text-lg p-3 pb-1">Productions at this venue:</h3>
                <div className="mx-3">
                    <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                        <div className="col-span-2 p-1">Name</div>
                        <div className="col-span-2 p-1">Venue</div>
                        <div className="col-span-2 p-1">Description</div>
                        <div className="col-span-2 p-1">Sundowners</div>
                        <div className="col-span-2 p-1">Author</div>
                        <div className="col-span-2 p-1">Date Created</div>
                    </div>
                    {productions.map((production, index) => (
                        <ProductionRow productionData={production} key={index} />
                    ))}
                </div>
            </div>
        }

        {festivals &&
            <div>
                <h3 className="font-bold text-lg p-3 pb-1">Festivals at this venue:</h3>
                <div className="mx-3">
                    <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                        <div className="col-span-2 p-1">Name</div>
                        <div className="col-span-2 p-1">Venue</div>
                        <div className="col-span-2 p-1">Description</div>
                        <div className="col-span-2 p-1">Year</div>
                        <div className="col-span-2 p-1">Month</div>
                        <div className="col-span-2 p-1">Date Created</div>
                    </div>
                    {festivals.map((festival, index) => (
                        <FestivalRow festivalData={festival} key={index} />
                    ))}
                </div>
            </div>

        }
    </>)
}

export default VenuePage