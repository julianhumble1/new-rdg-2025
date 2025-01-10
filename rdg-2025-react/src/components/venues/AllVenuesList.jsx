import { useEffect, useState } from "react"
import VenueService from "../../services/VenueService.js"
import VenueRow from "./VenueRow.jsx"

const AllVenuesList = () => {

    const [venues, setVenues] = useState([])

    const [fetchError, setFetchError] = useState("")

    useEffect(() => {

        const fetchAllVenues = async () => {
            try {
                const response = await VenueService.getAllVenues();
                setVenues(response.data.venues)
            } catch (e) {
                setFetchError(e.message)
            }
        }

        fetchAllVenues();
        
    }, [])


    return (<>
        <div>List of all venues</div>
        {fetchError && 
            <div className="text-red-500">
                {fetchError}
            </div>
        }

        {venues &&
            <div className="mx-3">
                <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                    <div className="col-span-2 p-1">Name</div>
                    <div className="col-span-2 p-1">Address</div>
                    <div className="col-span-2 p-1">Town</div>
                    <div className="col-span-2 p-1">Postcode</div>
                    <div className="col-span-2 p-1">Date Created</div>
                    <div className="col-span-2 p-1">Notes</div>
                </div>
                {venues.map((venue, index) => (
                    <VenueRow venueData={venue} key={index} />
                ))}
            </div>
        }
        

  </>)
}

export default AllVenuesList