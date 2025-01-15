import { useEffect, useState } from "react"
import Select from "react-select";
import VenueService from "../../services/VenueService.js";
import FestivalService from "../../services/FestivalService.js";

const NewFestivalForm = () => {

    const currentYear = new Date().getFullYear();

    const [name, setName] = useState("")
    const [venue, setVenue] = useState({ label: "None", value: 0 })
    const [year, setYear] = useState({ value: currentYear, label: currentYear })
    const [month, setMonth] = useState({value: 1, label: "January"})
    const [description, setDescription] = useState("")

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const [venueOptions, setVenueOptions] = useState([])

    const [yearOptions, setYearOptions] = useState([])

    const monthOptions = [
        { value: 1, label: "January" },
        { value: 2, label: "February" },
        { value: 3, label: "March" },
        { value: 4, label: "April" },
        { value: 5, label: "May" },
        { value: 6, label: "June" },
        { value: 7, label: "July" },
        { value: 8, label: "August" },
        { value: 9, label: "September" },
        { value: 10, label: "October" },
        { value: 11, label: "November" },
        { value: 12, label: "December" }
    ];

    const handleSubmit = async (event) => {
        event.preventDefault()

        try {
            const response = await FestivalService.createNewFestival(
                name, venue.value, year.value, month.value, description
            )
            setFailMessage("")
            setSuccessMessage(`Successfully created '${response.data.festival.name}'!`)
        } catch (e) {
            setSuccessMessage("")
            setFailMessage(e.message)
        }
    }


    useEffect(() => {
        const getVenues = async () => {
            try {
                const response = await VenueService.getAllVenues()
                const venueList = response.data.venues
                const formattedVenueList = venueList.map((venue) => ({ "value": venue.id, "label": venue.name }))
                setVenueOptions(formattedVenueList)
            } catch (e) {
                setFailMessage(e.message)
            }
        }
        getVenues()
    }, [])
    
    useEffect(() => {
        const yearsArray = [];
        for (let i = 0; i < 10; i++) {
            yearsArray.push({"value": currentYear + 1, "label": currentYear + i})
        }
        setYearOptions(yearsArray)
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
                <Select options={monthOptions} onChange={setMonth} className="w-fit" isClearable value={month} />
            </div>

            <div>
                <div className="italic">
                    Description
                </div>
                <input placeholder="A drama festival" className="border p-1" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <button className={`bg-green-300 px-3 py-1 w-fit rounded hover:bg-green-600 ${!name && "cursor-not-allowed"}`} >Submit</button>
            {successMessage && 
                <div className="text-green-500">
                    {successMessage}
                </div>
            }
            {failMessage && 
                <div className="text-red-500">
                    Failed to add production: {failMessage}
                </div>
            }
        
        
        </form>
    </>)
}

export default NewFestivalForm