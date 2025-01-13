import { useState } from "react"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";

const NewProductionForm = () => {

    const [name, setName] = useState("")
    const [venue, setVenue] = useState("")
    const [author, setAuthor] = useState("")
    const [description, setDescription] = useState("")
    const [auditionDate, setAuditionDate] = useState(new Date())
    const [sundowners, setSundowners] = useState(false)
    const [notConfirmed, setNotConfirmed] = useState(false)
    const [flyerFile, setFlyerFile] = useState("")

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const handleSubmit = async (event) => {
        event.preventDefault()
    }

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
                <input placeholder="The Globe" className="border p-1" value={venue} onChange={(e) => setVenue(e.target.value)}  />
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
              {/* <input placeholder="12/12/12" className="border p-1" value={auditionDate} onChange={(e) => setAuditionDate(e.target.value)}/> */}
              <DatePicker className="border rounded p-1" selected={auditionDate} onChange={(date) => setAuditionDate(date)} dateFormat="dd/MM/yyyy"/>
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