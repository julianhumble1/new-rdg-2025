import { useState } from "react"
import { Label, Textarea, TextInput } from "flowbite-react"

const EditVenueForm = ({ venueData, handleEdit, setEditMode }) => {

    const [name, setName] = useState(venueData.name)
    const [address, setAddress] = useState(venueData.address ? venueData.address : "")
    const [town, setTown] = useState(venueData.town ? venueData.town : "")
    const [postcode, setPostcode] = useState(venueData.postcode ? venueData.postcode : "")
    const [notes, setNotes] = useState(venueData.notes ? venueData.notes : "")
    const [url, setUrl] = useState(venueData.url ? venueData.url : "")

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleEdit(event, venueData.id, name, address, town, postcode, notes, url)}>
                <div>
                    <div className="mb-2 block">
                        <Label value="Name (required)" />
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
                    <button className="text-sm hover:underline font-bold text-center col-span-1" onClick={() => setEditMode(false)}>
                        Cancel Edit Mode
                    </button>
                    <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
                        Confirm Edit
                    </button>
                </div>
            </form>
        </div>
    )
}

export default EditVenueForm