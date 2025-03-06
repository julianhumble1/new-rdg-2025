import { useState } from "react"
import PersonService from "../../services/PersonService.js"
import { Label, TextInput } from "flowbite-react"
import { Link } from "react-router-dom"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import SuccessMessage from "../modals/SuccessMessage.jsx"

const NewPersonForm = () => {

	const [firstName, setFirstName] = useState("")
	const [lastName, setLastName] = useState("")
	const [summary, setSummary] = useState("")
	const [homePhone, setHomePhone] = useState("")
	const [mobilePhone, setMobilePhone] = useState("")
	const [addressStreet, setAddressStreet] = useState("")
	const [addressTown, setAddressTown] = useState("")
	const [addressPostcode, setAddressPostcode] = useState("")

	const [successMessage, setSuccessMessage] = useState("")
	const [errorMessage, setErrorMessage] = useState("")

	const handleSubmit = async (event) => {
		event.preventDefault()
		try {
			const response = await PersonService.addNewPerson(
				firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode
			)
			console.log(response)
			setSuccessMessage(`Successfully created ${response.data.person.firstName} ${response.data.person.lastName}`)
		} catch (e) {
			setErrorMessage(e.message)
		}
	}

    return (
		<div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 rounded p-4 m-2 flex flex-col gap-2 shadow-md">
			<form className="flex flex-col gap-2 max-w-md" onSubmit={(event) => handleSubmit(event)}>
				<SuccessMessage message={successMessage} />
				<ErrorMessage message={errorMessage} />
				<div >
                    <div className="mb-2 block">
                        <Label value="Name (required)" />
					</div>
					<div className="flex md:flex-row flex-col gap-1 w-full">
						<TextInput placeholder="First Name" className="flex-auto" required value={firstName} onChange={(e) => setFirstName(e.target.value)} />
						<TextInput placeholder="Last Name" className="flex-auto" required value={lastName} onChange={(e) => setLastName(e.target.value)}/>
					</div>
				</div>
				
				<div>
                    <div className="mb-2 block">
                        <Label value="Contact Details" />
					</div>
					<div className="flex md:flex-row flex-col gap-1 w-full">
                    	<TextInput placeholder="Home Phone" className="flex-auto" value={homePhone} onChange={(e) => setHomePhone(e.target.value)}/>
						<TextInput placeholder="Mobile Phone" className="flex-auto" value={mobilePhone} onChange={(e) => setMobilePhone(e.target.value)}/>
					</div>
				</div>
				
				<div>
                    <div className="mb-2 block">
                        <Label value="Address" />
					</div>
					<div className="flex flex-col gap-2">
						<TextInput placeholder="Street" className="w-full" value={addressStreet} onChange={(e) => setAddressStreet(e.target.value)} />
						<div className="flex md:flex-row gap-1">
							<TextInput placeholder="Town" className="flex-auto" value={addressTown} onChange={(e) => setAddressTown(e.target.value)} />
							<TextInput placeholder="Postcode" className="flex-auto" value={addressPostcode} onChange={(e) => setAddressPostcode(e.target.value)}/>
						</div>
					</div>
				</div>
				
				
				<div className="grid grid-cols-2 justify-end px-2 mt-2">
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

export default NewPersonForm