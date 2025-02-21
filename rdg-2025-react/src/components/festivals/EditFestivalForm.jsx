import { useState, useEffect } from "react"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import MonthDateUtils from "../../utils/MonthDateUtils.js"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import { Label, Textarea, TextInput } from "flowbite-react"
import Select from "react-select"

const EditFestivalForm = ({festivalData, handleEdit, setEditMode}) => {

    const [venueOptions, setVenueOptions] = useState([])
    const [yearOptions, setYearOptions] = useState([])

    const [name, setName] = useState(festivalData.name)
    const [venue, setVenue] = useState(festivalData.venue ? 
        { label: festivalData.venue.name, value: festivalData.venue.id } : 
        { label: "None", value: 0 }
    )
    const [year, setYear] = useState({value: festivalData.year, label: festivalData.year})
    const [month, setMonth] = useState(festivalData.month ? {value: festivalData.month, label: MonthDateUtils.monthMapping[festivalData.month]} : "")
    const [description, setDescription] = useState(festivalData.description ? festivalData.description : "")

    const [descriptionLength, setDescriptionLength] = useState(festivalData.description ? festivalData.description.length : 0)

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
        setYearOptions(MonthDateUtils.getYearsArray)
    }, [])

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
            <ErrorMessage message={errorMessage} />
            <form className="flex flex-col gap-2" onSubmit={(event) => handleEdit(event, festivalData.id, name, venue.value, year.value, month.value, description)}>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Festival Name (required)" />
                    </div>
                    <TextInput placeholder="Edinburgh Fringe" required value={name} onChange={(e) => setName(e.target.value)}/>
                </div>

                <div>
                    <div className="mb-2 block italic">
                        <Label value="Venue" />
                    </div>
                    <Select options={venueOptions} onChange={setVenue} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}} isClearable defaultValue={venue}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Year (required)" />
                    </div>
                    <Select options={yearOptions} defaultValue={year} onChange={setYear} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}} value={year} required={true} />
                </div>

                <div>
                    <div className="mb-2 block italic">
                        <Label value="Month" />
                    </div>
                    <Select options={MonthDateUtils.monthOptions} onChange={setMonth} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}} isClearable value={month} defaultValue={month}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value={`Description (max 2000 characters, current: ${descriptionLength})`} />
                    </div>
                    <Textarea placeholder="A drama festival that takes place every year in Woking. " value={description} onChange={(e) => setDescription(e.target.value)} onBlur={(e) => setDescriptionLength(e.target.value.length)} rows={4} />
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

export default EditFestivalForm
