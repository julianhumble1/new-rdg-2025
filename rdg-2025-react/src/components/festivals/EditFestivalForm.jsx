import { useEffect, useState } from "react"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import MonthDateUtils from "../../utils/MonthDateUtils.js"
import Select from "react-select"

const EditFestivalForm = ({ festivalData, handleEdit }) => {
    
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
        <form className="flex flex-col gap-2" onSubmit={(event) => handleEdit(event, festivalData.id, name, venue.value, year.value, month.value, description )}>
            <div >
                <div className="italic">
                    Festival Name (required)
                </div>
                <input placeholder="Edinburgh Fringe" className="border p-1" value={name} onChange={(e) => setName(e.target.value)} required={true} />
            </div>
            <div>
                <div className="italic">
                    Venue
                </div>
              <Select options={venueOptions} onChange={setVenue} className="w-fit" isClearable defaultValue={venue}/>
            </div>
            <div>
                <div className="italic">
                    Year (required)
                </div>
                <Select options={yearOptions} defaultValue={year} onChange={setYear} className="w-fit" value={year} required={true} />
            </div>
            <div>
                <div className="italic">
                    Month
                </div>
                <Select options={MonthDateUtils.monthOptions} onChange={setMonth} className="w-fit" isClearable value={month} defaultValue={month}/>
            </div>

            <div>
                <div className="italic">
                    Description
                </div>
                <input placeholder="A drama festival" className="border p-1" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>

            <button className={`bg-green-300 px-3 py-1 w-fit rounded hover:bg-green-600 ${!name && "cursor-not-allowed"}`} >Submit</button>
        </form>
    )
}

export default EditFestivalForm