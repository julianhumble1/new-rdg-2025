import { useEffect, useState } from "react"
import { useParams, useNavigate, Link } from "react-router-dom"
import PerformanceService from "../../services/PerformanceService.js"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import { Label, Textarea, TextInput } from "flowbite-react"
import Select from "react-select"
import DatePicker from "react-datepicker"
import "react-datepicker/dist/react-datepicker.css";
import { CurrencyPoundIcon } from "@heroicons/react/16/solid"

const EditPerformanceForm = () => {

    const performanceId = useParams().id
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

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    useEffect(() => {
        const setOptions = async () => {
            try {
                setProductionOptions(await FetchValueOptionsHelper.fetchProductionOptions())
                setVenueOptions(await FetchValueOptionsHelper.fetchVenueOptions())
                setFestivalOptions(await FetchValueOptionsHelper.fetchFestivalOptions())
            } catch (e) {
                setErrorMessage(e.message)
            }
        }
        setOptions()
    }, [])

    useEffect(() => {
        const getPerformanceId = async () => {
            try {
                const response = await PerformanceService.getPerformanceById(performanceId)
                console.log(response)
                setProduction({ value: response.data.performance.production.id, label: `${response.data.performance.production.name}` })
                setVenue({ value: response.data.performance.venue.id, label: `${response.data.performance.venue.name}` })
                setFestival(response.data.performance.festival ? 
                    { value: response.data.performance.festival.id, label: `${response.data.performance.festival.name} (${response.data.performance.festival.year})` } : {
                        value: 0, label: "None"
                    }
                )
                setPerformanceTime(new Date(response.data.performance.time))
                setDescription(response.data.performance.description)
                setStandardPrice(response.data.performance.standardPrice || "");
                setConcessionPrice(response.data.performance.concessionPrice || "");
                setBoxOffice(response.data.performance.boxOffice || "");
            } catch (e) {
                setErrorMessage(e.message)
            }
        }
        getPerformanceId()
    }, [performanceId])

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            const response = await PerformanceService.updatePerformance(
                performanceId,
                production.value,
                venue.value,
                festival ? festival.value : "",
                performanceTime,
                description,
                standardPrice,
                concessionPrice, 
                boxOffice
            )
            navigate(`/productions/${response.data.performance.production.id}`)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }


    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} /> 
            <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
                <form className="flex flex-col gap-2 max-w-md" onSubmit={handleSubmit} >
                    <div>
                        <div className="mb-2 block">
                            <Label value="Production (required)" />
                        </div>
                        <Select options={productionOptions} value={production} required
                            onChange={(selectedOption) => {
                                setProduction(selectedOption)
                                selectedOption.venue ? setVenue({label: selectedOption.venue.name, value: selectedOption.venue.id}) : setVenue(null)
                        }} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                        <div className="text-xs m-2">
                            If production has an associated venue, this will be automatically filled below. Can be overridden if necessary.
                        </div>
                    </div>
                    <div>
                        <div className="mb-2 block">
                            <Label value="Venue (required)" />
                        </div>
                        <Select options={venueOptions} required onChange={setVenue} value={venue} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                    </div>
                    <div>
                        <div className="mb-2 block">
                            <Label value="Festival" />
                        </div>
                        <Select options={festivalOptions} onChange={setFestival} isClearable value={festival} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                    </div>
                    <div>
                        <div className="mb-2 block">
                            <Label value="Date & Time (required)" />
                        </div>
                        <DatePicker selected={performanceTime} onChange={(time) => setPerformanceTime(time)}  dateFormat="dd/MM/yyyy h:mm aa" showTimeSelect timeIntervals={15} popperPlacement="right" showIcon className="p-1 rounded border border-gray-300 text-sm"/>
                    </div>
                    <div>
                        <div className="mb-2 block italic">
                            <Label value={`Description (max 2000 characters, current: ${description.length})`} />
                        </div>
                        <Textarea placeholder="Divisional Final of the All-England Theatre Festival" value={description} onChange={(e) => setDescription(e.target.value)} rows={4} />
                    </div>
                    <div>
                        <Label value="Standard Price (£)" />
                        <TextInput type="number"
                            icon={CurrencyPoundIcon}
                            value={standardPrice}
                            step={0.01}
                            onBlur={(e) => {
                                const value = parseFloat(e.target.value).toFixed(2);
                                e.target.value = value;
                            }}
                            onChange={(e) => setStandardPrice(e.target.value)}
                        />
                    </div>
                    <div>
                        <Label value="Concession Price (£)" />
                        <TextInput type="number"
                            icon={CurrencyPoundIcon}
                            value={concessionPrice}
                            step={0.01}
                            onBlur={(e) => {
                                const value = parseFloat(e.target.value).toFixed(2);
                                e.target.value = value;
                            }}
                            onChange={(e) => setConcessionPrice(e.target.value)}
                        />
                    </div>
                    <div>
                        <div className="mb-2 block">
                            <Label value="Box Office" />
                        </div>
                        <TextInput placeholder="www.boxoffice.com" value={boxOffice} onChange={(e) => setBoxOffice(e.target.value)} />
                    </div>
                    <div className="grid grid-cols-2 justify-end px-2">
                        <Link to="/dashboard" className="text-sm hover:underline font-bold text-center col-span-1 my-auto" >
                            Cancel
                        </Link>
                        <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
                            Submit
                        </button>
                    </div>
                </form>
            </div>
        </div>
    )
}

export default EditPerformanceForm