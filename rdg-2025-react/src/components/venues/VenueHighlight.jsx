import { format } from "date-fns"
import AddressHelper from "../../utils/AddressHelper.js"
import { Link } from "react-router-dom"

const VenueHighlight = ({ venueData, setEditMode, handleDelete }) => {
    
    const fullAddress = AddressHelper.getFullAddress(venueData.address, venueData.town, venueData.postcode)

    if (venueData) return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <div className="text-black text-xl font-bold flex justify-between">
                <div>
                    {venueData.name}
                </div>
                <div className="flex gap-2">
                    <button className="text-sm hover:underline" onClick={() => setEditMode(true)}>
                        Edit
                    </button>
                    <button className="text-sm hover:underline" onClick={() => handleDelete(venueData)}>
                        Delete
                    </button>
                </div>
            </div>
            {fullAddress &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Address
                    </div>
                    <div>
                        {fullAddress}
                    </div>
                </div>       
            }
            {venueData.notes &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Notes
                    </div>
                    <div>
                        {venueData.notes}
                    </div>
                </div>       
            }
            {venueData.url &&
                <div className="flex flex-col">
                    <div className="font-bold italic">
                        Website
                    </div>
                    <a href={venueData.url } className="text-blue-500 hover:text-blue-700 hover:underline" target="_blank">
                        {venueData.url}
                    </a>
                </div>       
            }
            <div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Created:
                    </div>    
                    <div>
                        {format(new Date(venueData.createdAt), "dd-MM-yyyy")}
                    </div>
                </div>
                <div className="flex text-sm gap-1">
                    <div className="font-bold italic">
                        Last Updated:
                    </div>    
                    <div>
                        {format(new Date(venueData.updatedAt), "dd-MM-yyyy")}
                    </div>
                </div>  
            </div>
            <Link to="/venues" className="text-sm hover:underline font-bold text-end">
                See All Venues
            </Link>
        </div>
    )
}

export default VenueHighlight