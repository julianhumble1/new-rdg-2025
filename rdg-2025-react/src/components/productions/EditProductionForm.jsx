import { useState, useEffect } from "react"
import VenueService from "../../services/VenueService.js"
import Select from "react-select"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";

const EditProductionForm = ({ productionData, handleEdit }) => {

    const [venueOptions, setVenueOptions] = useState([])

    const [name, setName] = useState(productionData.name)
    const [venue, setVenue] = useState(productionData.venue ? 
        { label: productionData.venue.name, value: productionData.venue.id } : 
        { label: "None", value: 0 }
    )
    const [author, setAuthor] = useState(productionData.author)
    const [description, setDescription] = useState(productionData.description)
    const [auditionDate, setAuditionDate] = useState(new Date(productionData.auditionDate))
    const [sundowners, setSundowners] = useState(productionData.sundowners)
    const [notConfirmed, setNotConfirmed] = useState(productionData.notConfirmed)
    const [flyerFile, setFlyerFile] = useState(productionData.flyerFile)

    const [failMessage, setFailMessage] = useState("")


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

    return (
        <form className="flex flex-col gap-2" onSubmit={(event) => handleEdit(event, productionData.id, name, venue ? venue.value : null, author, description, auditionDate, sundowners, notConfirmed, flyerFile)}>
            <div>
                <div className="italic rounded">
                    Production Name (required)
                </div>
                <input placeholder="Oliver!" className="border p-1" value={name} onChange={(e) => setName(e.target.value)} required={true} />
            </div>
            <div>
                <div className="italic">
                    Venue
                </div>
              <Select options={venueOptions} onChange={setVenue} className="w-fit" isClearable defaultValue={ venue }/>
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

        </form>
    )
}

export default EditProductionForm