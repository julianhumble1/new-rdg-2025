import VenueRow from "./VenueRow.jsx"

const VenuesTable = ({ venues, handleDelete }) => {
    
    if (venues.length > 0) {
        return (
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
        )
    } else {
        return <div className="font-bold m-3">No venues to display</div>
    }
  
}

export default VenuesTable