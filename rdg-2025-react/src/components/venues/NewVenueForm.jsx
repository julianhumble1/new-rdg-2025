import { useState } from "react"
import VenueService from "../../services/VenueService.js"

const NewVenueForm = () => {

    const [name, setName] = useState("")
    const [address, setAddress] = useState("")
    const [town, setTown] = useState("")
    const [postcode, setPostcode] = useState("")
    const [notes, setNotes] = useState("")
    const [url, setUrl] = useState("")

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            const response = await VenueService.createNewVenue(name, address, town, postcode, notes, url)
            setSuccessMessage(`Successfully created '${response.data.venue.name}!'`)
            setFailMessage("")
        } catch (e) {
            setSuccessMessage("")
            setFailMessage(e.message)
        }
    }

    return (<>
        <div>Add a New Venue</div>
        <form onSubmit={handleSubmit} className="m-3 p-2 mt-1 border border-black rounded flex flex-col gap-2 w-1/2">
            <div>
                <div className="italic">
                    Venue Name (required)
                </div>
                <input placeholder="The Globe" className="border p-1" value={name} onChange={(e) => setName(e.target.value)} required={true} />
            </div>
            <div>
                <div className="italic">
                    Address
                </div>
                <input placeholder="21 New Globe Walk" className="border p-1" value={address} onChange={(e) => setAddress(e.target.value)}  />
            </div>
            <div>
                <div className="italic">
                    Town
                </div>
                <input placeholder="London" className="border p-1" value={town} onChange={(e) => setTown(e.target.value)}  />
            </div>
            <div>
                <div className="italic">
                    Postcode
                </div>
                <input placeholder="SE1 9DT" className="border p-1" value={postcode} onChange={(e) => setPostcode(e.target.value)} />
            </div>
            <div>
                <div className="italic">
                    Notes
                </div>
                <input placeholder="Parking is round the back" className="border p-1" value={notes} onChange={(e) => setNotes(e.target.value)}/>
            </div>
            <div>
                <div className="italic">
                    Website
                </div>
                <input placeholder="www.theglobe.com" className="border p-1" value={url} onChange={(e) => setUrl(e.target.value)}/>
            </div>
            <button className={`bg-green-300 px-3 py-1 w-fit rounded hover:bg-green-600 ${!name && "cursor-not-allowed"}`} >Submit</button>
            {successMessage && 
                <div className="text-green-500">
                    {successMessage}
                </div>
            }
            {failMessage && 
                <div className="text-red-500">
                    Failed to add venue: {failMessage}
                </div>
            }
        </form>
    </>)
}

export default NewVenueForm