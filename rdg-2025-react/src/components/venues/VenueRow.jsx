import DateHelper from "../../utils/DateHelper.js"

const VenueRow = ({ venueData, handleDelete }) => {

    return (
        <div className="bg-gray-200 grid grid-cols-12 h-fit hover:bg-gray-300">
            <div className="col-span-2 p-1"> {venueData.name} </div>
            <div className="col-span-2 p-1"> {venueData.address} </div>
            <div className="col-span-1 p-1"> {venueData.town} </div>
            <div className="col-span-1 p-1"> {venueData.postcode} </div>
            <div className="col-span-2 p-1"> {DateHelper.formatDatabaseDateForDisplay(venueData.createdAt)} </div>
            <div className="col-span-2 p-1"> {venueData.notes} </div>
            <div className="col-span-2 p-1">
                <button className="underline text-blue-500 hover:text-blue-700" onClick={() => handleDelete(venueData)}>Delete</button>
            </div>
        </div>
  )
}

export default VenueRow