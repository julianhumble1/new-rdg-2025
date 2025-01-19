import { useEffect, useState } from "react"
import VenueService from "../../services/VenueService.js"
import VenueRow from "./VenueRow.jsx"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx"

const AllVenuesList = () => {

    const [venues, setVenues] = useState([])

    const [fetchError, setFetchError] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState("");
    const [venueToDelete, setVenueToDelete] = useState(null);
    const [deleteError, setDeleteError] = useState("")
    const [deleteSuccess, setDeleteSuccess] = useState("")



    const handleDelete = (venue) => {
        setShowConfirmDelete(true)
        setVenueToDelete(venue)
    }

    const handleConfirmDelete = async (venue) => {
        try {
            await VenueService.deleteVenue(venue.id)
            setShowConfirmDelete(false)
            setVenueToDelete(null)
            setDeleteSuccess(`Successfully deleted '${venue.name}'`)
            fetchAllVenues()
        } catch (e) {
            setDeleteError(e.message)
        }
    }

    const fetchAllVenues = async () => {
        try {
            const response = await VenueService.getAllVenues();
            setVenues(response.data.venues)
        } catch (e) {
            setFetchError(e.message)
        }
    }

    useEffect(() => {
        fetchAllVenues();
    }, [])


    return (<>
        <div>List of all venues</div>
        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} venueToDelete={venueToDelete} handleConfirmDelete={ handleConfirmDelete } />
        }
        {deleteError &&
            <div className="text-red-500">
                {deleteError}
            </div>
        }
        {fetchError && 
            <div className="text-red-500">
                {fetchError}
            </div>
        }
        {deleteSuccess &&
            <div className="text-green-500">
                {deleteSuccess}
            </div>
        }

        {venues &&
            <div className="mx-3">
                <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                    <div className="col-span-2 p-1">Name</div>
                    <div className="col-span-2 p-1">Address</div>
                    <div className="col-span-1 p-1">Town</div>
                    <div className="col-span-1 p-1">Postcode</div>
                    <div className="col-span-2 p-1">Date Created</div>
                    <div className="col-span-2 p-1">Notes</div>
                    <div className="col-span-2 p-1">Actions</div>
                </div>
                {venues.map((venue, index) => (
                    <VenueRow venueData={venue} key={index} handleDelete={handleDelete} />
                ))}
            </div>
        }
        

  </>)
}

export default AllVenuesList