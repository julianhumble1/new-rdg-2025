import { useEffect, useState } from "react"
import Select from "react-select";
import FestivalService from "../../services/FestivalService.js";
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js";
import MonthDateUtils from "../../utils/MonthDateUtils.js";
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx";

const NewFestivalForm = () => {

    const currentYear = new Date().getFullYear();

    const [name, setName] = useState("")
    const [venue, setVenue] = useState({ label: "None", value: 0 })
    const [year, setYear] = useState({ value: currentYear, label: currentYear })
    const [month, setMonth] = useState({value: 1, label: "January"})
    const [description, setDescription] = useState("")

    const [venueOptions, setVenueOptions] = useState([])
    const [yearOptions, setYearOptions] = useState([])

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            const response = await FestivalService.createNewFestival(
                name, venue.value, year.value, month.value, description
            )
            setSuccessMessage(`Successfully created '${response.data.festival.name}'!`)
            setErrorMessage("")
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
        setYearOptions(MonthDateUtils.getYearsArray)
    }, [])


    return (<>
        <div>Add a New Festival</div>
        <form onSubmit={handleSubmit} className="m-3 p-2 mt-1 border border-black rounded flex flex-col gap-2 w-1/2">
            <div>
                <div className="italic">
                    Festival Name (required)
                </div>
                <input placeholder="Edinburgh Fringe" className="border p-1" value={name} onChange={(e) => setName(e.target.value)} required={true} />
            </div>
            <div>
                <div className="italic">
                    Venue
                </div>
              <Select options={venueOptions} onChange={setVenue} className="w-fit" isClearable/>
            </div>
            <div>
                <div className="italic">
                    Year (required)
                </div>
                <Select options={yearOptions} default={year} onChange={setYear} className="w-fit" value={year} required={true} />
            </div>
            <div>
                <div className="italic">
                    Month
                </div>
                <Select options={MonthDateUtils.monthOptions} onChange={setMonth} className="w-fit" isClearable value={month} />
            </div>

            <div>
                <div className="italic">
                    Description
                </div>
                <input placeholder="A drama festival" className="border p-1" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <button className={`bg-green-300 px-3 py-1 w-fit rounded hover:bg-green-600 ${!name && "cursor-not-allowed"}`} >Submit</button>
            
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
        
        
        </form>
    </>)
}

export default NewFestivalForm