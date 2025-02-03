import { useEffect, useState } from "react"
import VenueService from "../../services/VenueService.js"
import VenueRow from "./VenueRow.jsx"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import VenuesTable from "./VenuesTable.jsx"

const AllVenuesList = () => {

    const [venues, setVenues] = useState([])

    const [showConfirmDelete, setShowConfirmDelete] = useState("");
    const [venueToDelete, setVenueToDelete] = useState(null);

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const handleDelete = (venue) => {
        setShowConfirmDelete(true)
        setVenueToDelete(venue)
    }

    const handleConfirmDelete = async (venue) => {
        try {
            await VenueService.deleteVenue(venue.id)
            setShowConfirmDelete(false)
            setVenueToDelete(null)
            setSuccessMessage(`Successfully deleted '${venue.name}'`)
            fetchAllVenues()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    const fetchAllVenues = async () => {
        try {
            const response = await VenueService.getAllVenues();
            setVenues(response.data.venues)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    useEffect(() => {
        fetchAllVenues();
    }, [])


    return (<>
        <div>List of all venues</div>

        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={venueToDelete} handleConfirmDelete={ handleConfirmDelete } />
        }

        <SuccessMessage message={successMessage} />
        <ErrorMessage message={errorMessage} />

        <VenuesTable venues={venues} handleDelete={handleDelete} />
        
  </>)
}

export default AllVenuesList