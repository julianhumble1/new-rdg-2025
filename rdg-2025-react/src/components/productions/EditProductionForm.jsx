import { Checkbox, Label, Textarea, TextInput } from "flowbite-react"
import { useEffect, useState } from "react"
import DatePicker from "react-datepicker"
import Select from "react-select"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import ErrorMessage from "../modals/ErrorMessage.jsx"

const EditProductionForm = ({ productionData, handleEdit, setEditMode }) => {

    const [venueOptions, setVenueOptions] = useState([])

    const [name, setName] = useState(productionData.name)
    const [venue, setVenue] = useState(productionData.venue ? 
        { label: productionData.venue.name, value: productionData.venue.id } : 
        { label: "None", value: 0 }
    )
    const [author, setAuthor] = useState(productionData.author ? productionData.author : "")
    const [description, setDescription] = useState(productionData.description ? productionData.description : "")
    const [auditionDate, setAuditionDate] = useState(productionData.auditionDate ? new Date(productionData.auditionDate) : new Date())
    const [sundowners, setSundowners] = useState(productionData.sundowners ? productionData.sundowners : false)
    const [notConfirmed, setNotConfirmed] = useState(productionData.notConfirmed ? productionData.notConfirmed : false)
    const [flyerFile, setFlyerFile] = useState(productionData.flyerFile ? productionData.flyerFile : "")

    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        const getVenues = async () => {
            try {
                setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions())
            } catch (e) {
                setErrorMessage(e.message)
            }
        }
        getVenues()
    }, [])

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
            <ErrorMessage message={errorMessage} />
            <form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleEdit(event, productionData.id, name, venue ? venue.value : null, author, description, auditionDate, sundowners, notConfirmed, flyerFile)}>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Production Name (required)" />
                    </div>
                    <TextInput placeholder="Oliver!" required value={name} onChange={(e) => setName(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Venue" />
                    </div>
                    <Select options={venueOptions} onChange={setVenue} className="w-full rounded" isClearable defaultValue={venue}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Author" />
                    </div>
                    <TextInput placeholder="William Shakespeare" value={author} onChange={(e) => setAuthor(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Description" />
                    </div>
                    <Textarea placeholder="A tale of a young orphan... " value={description} onChange={(e) => setDescription(e.target.value)} rows={4} />
                </div>
                <div className="grid grid-cols-2">
                    <div>
                        <div className="italic">
                            <Label value="Audition Date" />
                        </div>
                        <DatePicker className="border border-gray-300 rounded p-2" selected={auditionDate} onChange={(date) => setAuditionDate(date)} dateFormat="dd/MM/yyyy" isClearable/>
                    </div>
                    <div className="flex flex-col justify-center gap-2">
                        <div className="flex justify-center gap-2">
                            <Label htmlFor="sundowners" className="flex italic">
                                Sundowners?
                            </Label>
                            <Checkbox checked={sundowners} onChange={(e) => setSundowners(e.target.checked)} />
                        </div>
                        <div className="col-span-1 flex justify-center gap-2">
                            <Label htmlFor="sundowners" className="flex italic">
                                Not Confirmed?
                            </Label>
                            <Checkbox checked={notConfirmed} onChange={(e) => setNotConfirmed(e.target.checked)} />
                        </div>
                    </div>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Flyer File" />
                    </div>
                    <TextInput placeholder="oliver-flyer.pdf"  value={flyerFile} onChange={(e) => setFlyerFile(e.target.value)}/>
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

export default EditProductionForm