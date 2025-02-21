import { format } from "date-fns"
import { useState, useEffect } from "react"
import ProductionService from "../../services/ProductionService.js"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import { Label, Textarea, TextInput, Checkbox } from "flowbite-react"
import Select from "react-select"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";
import { Link, useNavigate } from "react-router-dom"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"

const NewProductionForm = () => {

    const navigate = useNavigate()

    const [name, setName] = useState("")
    const [venue, setVenue] = useState({label: "None", value: 0})
    const [author, setAuthor] = useState("")
    const [description, setDescription] = useState("")
    const [auditionDate, setAuditionDate] = useState("")
    const [sundowners, setSundowners] = useState(false)
    const [notConfirmed, setNotConfirmed] = useState(false)
    const [flyerFile, setFlyerFile] = useState("")

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [venueOptions, setVenueOptions] = useState([])

    const [descriptionLength, setDescriptionLength] = useState(0)

    const handleSubmit = async (event) => {
        event.preventDefault()

        try {
            const response = await ProductionService.createNewProduction(
                name,
                venue.value,
                author,
                description,
                auditionDate ? format(auditionDate, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS") : "",
                sundowners, 
                notConfirmed,
                flyerFile
            )
            setSuccessMessage(`Successfully created '${response.data.production.name}'!`)
            setErrorMessage("")
            navigate(`/productions/${response.data.production.id}`)
        } catch (e) {
            setSuccessMessage("")
            setErrorMessage(e.message)
        }

    }

    useEffect(() => {
        const getVenueOptions = async () => {
            try {
                setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions())
            } catch (e) {
                setErrorMessage(e.message)
            }
        }
        getVenueOptions()
    }, [])


    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit}>
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
                    <Select options={venueOptions} onChange={setVenue} className="w-full text-sm" isClearable styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Author" />
                    </div>
                    <TextInput placeholder="William Shakespeare" value={author} onChange={(e) => setAuthor(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value={`Description (max 2000 characters, current: ${descriptionLength})`} />
                    </div>
                    <Textarea placeholder="A tale of a young orphan... " value={description} onChange={(e) => setDescription(e.target.value)} onBlur={(e) => setDescriptionLength(e.target.value.length)} rows={4} />
                </div>
                <div className="grid grid-cols-2">
                    <div>
                        <div className="italic">
                            <Label value="Audition Date" />
                        </div>
                        <DatePicker className="border border-gray-300 rounded p-2 text-sm" selected={auditionDate} onChange={(date) => setAuditionDate(date)} dateFormat="dd/MM/yyyy" isClearable/>
                    </div>
                    <div className="flex flex-col justify-center gap-2">
                        <div className="flex justify-center gap-2">
                            <Label htmlFor="sundowners" className="flex italic">
                                Sundowners?
                            </Label>
                            <Checkbox checked={sundowners} onChange={(e) => setSundowners(e.target.checked)} />
                        </div>
                        <div className="col-span-1 flex justify-center gap-2">
                            <Label className="flex italic">
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
                    <Link to="/productions" className="text-sm hover:underline font-bold text-center col-span-1 my-auto" >
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

export default NewProductionForm