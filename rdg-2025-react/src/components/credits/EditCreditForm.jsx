import { useEffect, useMemo, useState } from "react"
import { Link, Navigate, useNavigate, useParams } from "react-router-dom"
import CreditService from "../../services/CreditService.js"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import { Label, Textarea, TextInput } from "flowbite-react"
import Select from "react-select"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"

const EditCreditForm = () => {

    const creditId = useParams().id
    const navigate = useNavigate()

    const typeOptions = useMemo(() => [
        { value: "ACTOR", label: "Acting" },
        { value: "MUSICIAN", label: "Musician" },
        { value: "PRODUCER", label: "Producing" }
    ], [])
    const [productionOptions, setProductionOptions] = useState([])
    const [personOptions, setPersonOptions] = useState([])
    
    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [name, setName] = useState("")
    const [type, setType] = useState(null)
    const [production, setProduction] = useState(null)
    const [person, setPerson] = useState(null)
    const [summary, setSummary] = useState("")

    const setOptions = async () => {
        setProductionOptions(await FetchValueOptionsHelper.fetchProductionOptions())
        setPersonOptions(await FetchValueOptionsHelper.fetchPersonOptions())
    }

    useEffect(() => {
        const fetchCreditData = async (creditId) => {
            const response = await CreditService.getCreditById(creditId);
            console.log(response)
            setName(response.data.credit.name)
            for (let i = 0; i < 3; i++) {
                if (response.data.credit.type === typeOptions[i].value) {
                    setType(typeOptions[i])
                }
            }
            setProduction({value: response.data.credit.production.id, label: response.data.credit.production.name})
            response.data.credit.person &&
                setPerson({ value: response.data.credit.person.id, label: `${response.data.credit.person.firstName} ${response.data.credit.person.lastName}` })
            setSummary(response.data.credit.summary)
        }

        try {
            setOptions()
            fetchCreditData(creditId)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [creditId, typeOptions])

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            console.log(creditId)
            const response = await CreditService.updateCredit(creditId, name, type.value, production.value, person.value, summary)
            navigate(`/productions/${response.data.credit.production.id}`)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} /> 
            <form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleSubmit(event)}>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Character Name / Musical Instrument / Producer Type (required)" />
                    </div>
                    <TextInput placeholder="Bill Sykes / Keyboards / Director" required value={name} onChange={(e) => setName(e.target.value)}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value="Type (required)" />
                    </div>
                    <Select options={typeOptions} onChange={setType} required className="w-full text-sm" value={type} styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Production (required)" />
                    </div>
                    <Select options={productionOptions} value={production} required onChange={(selectedOption) => {setProduction(selectedOption)}} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                </div>
                <div>
                    <div className="mb-2 block">
                        <Label value="Person" />
                    </div>
                    <Select options={personOptions} value={person} isClearable  onChange={(selectedOption) => {setPerson(selectedOption)}} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
                </div>
                <div>
                    <div className="mb-2 block italic">
                        <Label value={`Summary (max 2000 characters, current: ${summary.length})`} />
                    </div>
                    <Textarea placeholder="An evil villain in his 40s... " value={summary} onChange={(e) => setSummary(e.target.value)} rows={4} />
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
    )
}

export default EditCreditForm