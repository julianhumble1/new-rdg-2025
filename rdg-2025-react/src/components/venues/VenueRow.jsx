import { Link } from "react-router-dom"
import { format } from "date-fns"

const VenueRow = ({ venueData, handleDelete }) => {

    return (
        <div className="bg-gray-200 grid grid-cols-12 h-fit hover:bg-gray-300">
            <div className="col-span-2 p-1">   
                <Link to={`/venues/${venueData.id}`} className="text-blue-500 hover:text-blue-700 hover:underline">
                    {venueData.name}
                </Link>
            </div>
            <div className="col-span-2 p-1"> {venueData.address} </div>
            <div className="col-span-1 p-1"> {venueData.town} </div>
            <div className="col-span-1 p-1"> {venueData.postcode} </div>
            <div className="col-span-2 p-1"> {format(new Date(venueData.createdAt), "dd-MM-yyyy")} </div>
            <div className="col-span-2 p-1"> {venueData.notes} </div>
            <div className="col-span-2 p-1 flex flex-row gap-2">
                <Link className="underline text-blue-500 hover:text-blue-700" to={`/venues/${venueData.id}?edit=true`}>Edit</Link>
                <button className="underline text-blue-500 hover:text-blue-700" onClick={() => handleDelete(venueData)}>Delete</button>
            </div>
        </div>
  )
}

export default VenueRow