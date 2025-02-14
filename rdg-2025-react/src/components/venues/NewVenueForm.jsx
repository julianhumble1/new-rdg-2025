import { useState } from "react"
import VenueService from "../../services/VenueService.js"
import { Link, useNavigate } from "react-router-dom"
import { Label, TextInput, Textarea } from "flowbite-react"
import ErrorMessage from "../modals/ErrorMessage.jsx"

const NewVenueForm = () => {
    const navigate = useNavigate()

    const [name, setName] = useState("")
    const [address, setAddress] = useState("")
    const [town, setTown] = useState("")
    const [postcode, setPostcode] = useState("")
    const [notes, setNotes] = useState("")
    const [url, setUrl] = useState("")

    const [errorMessage, setErrorMessage] = useState("")

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            const response = await VenueService.createNewVenue(name, address, town, postcode, notes, url)
            navigate(`/venues/${response.data.venue.id}`)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
            <form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleSubmit(event, name, address, town, postcode, notes, url)}>
                <ErrorMessage message={errorMessage} />
                <div>
                    <div className="mb-2 block">
                        <Label value="Venue Name (required)" />
                    </div>
                    <TextInput placeholder="The Globe" required value={name} onChange={(e) => setName(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Address" />
                    </div>
                    <TextInput placeholder="21 New Globe Walk" value={address} onChange={(e) => setAddress(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Town" />
                    </div>
                    <TextInput placeholder="London" value={town} onChange={(e) => setTown(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Postcode" />
                    </div>
                    <TextInput placeholder="SE1 9DT" value={postcode} onChange={(e) => setPostcode(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Notes" />
                    </div>
                    <Textarea placeholder="Parking is round the back" value={notes} onChange={(e) => setNotes(e.target.value)} rows={4} />
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Website" />
                    </div>
                    <TextInput placeholder="www.theglobe.com" value={url} onChange={(e) => setUrl(e.target.value)} />
                </div>
                <div className="grid grid-cols-2 justify-end px-2">
                    <Link to="/venues" className="text-sm hover:underline font-bold text-center col-span-1 my-auto" >
                        Cancel
                    </Link>
                    <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
                        Submit
                    </button>
                </div>
            </form>
        </div>
    )
}

export default NewVenueForm