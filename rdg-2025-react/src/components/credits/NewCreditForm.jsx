import { useEffect, useState } from "react"

import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import Select from "react-select"
import { Label, Textarea, TextInput } from "flowbite-react"
import FetchValueOptionsHelper from "../../utils/FetchValueOptionsHelper.js"
import { Link } from "react-router-dom"
import CreditService from "../../services/CreditService.js"

const NewCreditForm = () => {

    const typeOptions = [
        { value: "ACTOR", label: "Acting" },
        { value: "MUSICIAN", label: "Musician" },
        { value: "PRODUCER", label: "Producing" }
    ]
    const [productionOptions, setProductionOptions] = useState([])
    const [personOptions, setPersonOptions] = useState([])
    
    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [name, setName] = useState("")
    const [type, setType] = useState(typeOptions[0])
    const [production, setProduction] = useState(null)
    const [person, setPerson] = useState(null)
    const [summary, setSummary] = useState("")

    useEffect(() => {
        const setOptions = async () => {
            setProductionOptions(await FetchValueOptionsHelper.fetchProductionOptions())
            setPersonOptions(await FetchValueOptionsHelper.fetchPersonOptions())
        }
        try {
            setOptions()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [])

    const handleSubmit = async (event) => {
        event.preventDefault()
        try {
            const response = await CreditService.addNewCredit(
                name, type.value, production.value, person ? person.value : 0, summary
            )
            setSuccessMessage(`Successfully added ${response.data.credit.name} for ${response.data.credit.production.name}!` )
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
                    <Select options={typeOptions} onChange={setType} required className="w-full text-sm" defaultValue={type} styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
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
                    <Select options={personOptions} value={person}  onChange={(selectedOption) => {setPerson(selectedOption)}} className="w-full text-sm"  styles={{control: (baseStyles) => ({...baseStyles, borderRadius: 8, padding: 1 })}}/>
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

export default NewCreditForm