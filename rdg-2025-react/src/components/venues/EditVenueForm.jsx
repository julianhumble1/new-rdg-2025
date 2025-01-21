import { useState } from "react"

const EditVenueForm = ({ venueData, handleEdit }) => {
    
    const [name, setName] = useState(venueData.name)
    const [address, setAddress] = useState(venueData.address)
    const [town, setTown] = useState(venueData.town)
    const [postcode, setPostcode] = useState(venueData.postcode)
    const [notes, setNotes] = useState(venueData.notes)
    const [url, setUrl] = useState(venueData.url)

    return (
        <form className="flex flex-col gap-2" onSubmit={(event) => handleEdit(event, venueData.id, name, address, town, postcode, notes, url)}>
            <div>
                <div className="italic">
                    Venue Name (required)
                </div>
                <input placeholder="The Globe" className="border p-1 rounded" required={true} value={name} onChange={(e) => setName(e.target.value)} />
            </div>
            <div>
                <div className="italic">
                    Address
                </div>
                <input placeholder="21 New Globe Walk" className="border p-1" value={address} onChange={(e) => setAddress(e.target.value)}/>
            </div>
            <div>
                <div className="italic">
                    Town
                </div>
                <input placeholder="London" className="border p-1" value={town} onChange={(e) => setTown(e.target.value)}/>
            </div>
            <div>
                <div className="italic">
                    Postcode
                </div>
                <input placeholder="SE1 9DT" className="border p-1" value={postcode} onChange={(e) => setPostcode(e.target.value)}  />
            </div>
            <div>
                <div className="italic">
                    Notes
                </div>
                <input placeholder="Parking is round the back" className="border p-1" value={notes} onChange={(e) => setNotes(e.target.value)} />
            </div>
            <div>
                <div className="italic">
                    Website
                </div>
                <input placeholder="www.theglobe.com" className="border p-1" value={url} onChange={(e) => setUrl(e.target.value)} />
            </div>
            <button className={`bg-green-500 px-3 py-1 w-fit rounded hover:bg-green-700 ${!name && "cursor-not-allowed"}`} >Submit</button>
            
        </form>
    )
}

export default EditVenueForm