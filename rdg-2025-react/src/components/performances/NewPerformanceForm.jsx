import { useState, useEffect } from "react"
import Select from "react-select"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";
import PerformanceService from "../../services/PerformanceService.js";
import { useNavigate } from "react-router-dom";

const NewPerformanceForm = () => {

    const navigate = useNavigate()

    const [production, setProduction] = useState(null)
    const [venue, setVenue] = useState(null)
    const [festival, setFestival] = useState(null)
    const [performanceTime, setPerformanceTime] = useState(new Date())
    const [description, setDescription] = useState("")
    const [standardPrice, setStandardPrice] = useState("")
    const [concessionPrice, setConcessionPrice] = useState("")
    const [boxOffice, setBoxOffice] = useState("")

    const [productionOptions, setProductionOptions] = useState([])
    const [venueOptions, setVenueOptions] = useState([])
    const [festivalOptions, setFestivalOptions] = useState([])

    const [failMessage, setFailMessage] = useState("")

    useEffect(() => {
        const setOptions = async () => {
            try {
                setProductionOptions(await FetchValueOptionsHelper.fetchProductionOptions())
                setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions())
                setFestivalOptions(await FetchValueOptionsHelper.fetchFestivalOptions())
            } catch (e) {
                setFailMessage(e.message)
            }
        }
        setOptions()
    }, [])

    const handleSubmit = async (event) => {
        event.preventDefault()

        try {
            const response = await PerformanceService.addNewPerformance(
                production.value,
                venue.value,
                festival ? festival.value : "",
                performanceTime,
                description,
                standardPrice,
                concessionPrice, 
                boxOffice
            )
            navigate(`/productions/${production.value}`)
        } catch (e) {
            setFailMessage(e.message)
        }
    }

    return (<>
        <div className="pl-1 font-bold " >Add a new performance</div>
        {failMessage &&
            <div className="text-red-500 pl-1">
                {failMessage}
            </div>
        }
        <form className="border border-black w-1/2 p-4 flex flex-col ml-3 gap-2"  onSubmit={handleSubmit}>
            <div>
                <div className="italic">Production (required)</div>
                <Select
                    options={productionOptions}
                    onChange={(selectedOption) => {
                        setProduction(selectedOption)
                        selectedOption.venue ? setVenue({label: selectedOption.venue.name, value: selectedOption.venue.id}) : setVenue(null)
                    }}
                    value={production}
                    required
                />
                <div className="text-sm">
                    If production has an associated venue, this will be automatically filled below. Can be overridden if necessary.
                </div>
            </div>
            <div>
                <div className="italic">Venue (required)</div>
                <Select options={venueOptions} onChange={setVenue} value={venue} />
            </div>
            <div>
                <div className="italic">Festival</div>
                <Select options={festivalOptions} onChange={setFestival} isClearable value={festival} />
            </div>
            <div>
                <div className="italic">Date (required)</div>
                <DatePicker className="border  p-1 rounded w-full" selected={performanceTime} onChange={(time) => setPerformanceTime(time)}  dateFormat="dd/MM/yyyy h:mm aa" showTimeSelect timeIntervals={15} showIcon popperPlacement="right"/>
            </div>
            <div>
                <div className="italic">Description</div>
                <input type="text" className="border p-1 w-full" placeholder="An amazing performance..." value={description} onChange={(e) => setDescription(e.target.value)} />
            </div>
            <div>
                <div className="italic">Standard Price</div>
                <input type="number"
                    className="border p-1 w-fit"
                    step={0.01}
                    onBlur={(e) => {
                        const value = parseFloat(e.target.value).toFixed(2);
                        e.target.value = value;
                    }}
                    onChange={(e) => setStandardPrice(e.target.value)}
                />
            </div>
            <div>
                <div className="italic">Concession Price</div>
                <input
                    type="number"
                    className="border p-1 w-fit"
                    step={0.01}
                    onBlur={(e) => {
                        const value = parseFloat(e.target.value).toFixed(2);
                        e.target.value = value;
                    }}
                    onChange={(e) => setConcessionPrice(e.target.value)}
                />
            </div>
            <div>
                <div className="italic">Box Office</div>
                <input type="text" className="border p-1 w-full" placeholder="www.boxoffice.com" value={boxOffice} onChange={(e) => setBoxOffice(e.target.value)} />
            </div>

            <button className={`bg-green-300 px-3 py-1 w-fit rounded hover:bg-green-600 ${(!venue || !production || !performanceTime) && "cursor-not-allowed"}`} >Submit</button>

        </form>
    </>)
}

export default NewPerformanceForm