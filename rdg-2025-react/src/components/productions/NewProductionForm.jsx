import { useEffect, useState } from "react"
import DatePicker from "react-datepicker"
import Select from "react-select";
import { format } from 'date-fns';
import "react-datepicker/dist/react-datepicker.css";
import VenueService from "../../services/VenueService.js";
import ProductionService from "../../services/ProductionService.js";

const NewProductionForm = () => {

    const [name, setName] = useState("")
    const [venue, setVenue] = useState({label: "None", value: 0})
    const [author, setAuthor] = useState("")
    const [description, setDescription] = useState("")
    const [auditionDate, setAuditionDate] = useState(new Date())
    const [sundowners, setSundowners] = useState(false)
    const [notConfirmed, setNotConfirmed] = useState(false)
    const [flyerFile, setFlyerFile] = useState("")

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const [venueOptions, setVenueOptions] = useState([])


    const handleSubmit = async (event) => {
        event.preventDefault()

        try {
            const response = await ProductionService.createNewProduction(
                name,
                venue.value,
                author,
                description,
                format(auditionDate, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS"),
                sundowners, 
                notConfirmed,
                flyerFile
            )
            setSuccessMessage(`Successfully created '${response.data.production.name}'!`)
            setFailMessage("")
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

  return (<>
        <div>Add a New Production</div>
        <form onSubmit={handleSubmit} className="m-3 p-2 mt-1 border border-black rounded flex flex-col gap-2 w-1/2">
            <div>
                <div className="italic">
                    Production Name (required)
                </div>
                <input placeholder="Oliver!" className="border p-1" value={name} onChange={(e) => setName(e.target.value)} required={true} />
            </div>
            <div>
                <div className="italic">
                    Venue
                </div>
              <Select options={venueOptions} onChange={setVenue} className="w-fit" isClearable/>
            </div>
            <div>
                <div className="italic">
                    Author
                </div>
                <input placeholder="William Shakespeare" className="border p-1" value={author} onChange={(e) => setAuthor(e.target.value)}  />
            </div>
            <div>
                <div className="italic">
                    Description
                </div>
                <input placeholder="A tale of a young orphan... " className="border p-1" value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div>
                <div className="italic">
                    Audition Date
                </div>
              <DatePicker className="border rounded p-1" selected={auditionDate} onChange={(date) => setAuditionDate(date)} dateFormat="dd/MM/yyyy" isClearable/>
            </div>
            <div className=" flex gap-2">
                <div className="italic">
                    Sundowners? 
                </div>
                <div>
                    <input type="checkbox" checked={sundowners} onChange={(e) => setSundowners(e.target.checked)}/>  
                </div>
            </div>
            <div className=" flex gap-2">
                <div className="italic">
                    Not Confirmed? 
                </div>
                <div>
                    <input type="checkbox" checked={notConfirmed} onChange={(e) => setNotConfirmed(e.target.checked)}/>  
                </div>
            </div>
            <div>
                <div className="italic">
                    Flyer file
                </div>
                <input placeholder="oliver-flyer.pdf" className="border p-1" value={flyerFile} onChange={(e) => setFlyerFile(e.target.value)} />
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

export default NewProductionForm