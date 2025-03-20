import { FileInput, Label, Textarea, TextInput } from "flowbite-react"
import { useState } from "react"

const EditPersonForm = ({ setEditMode, handleEditPerson, personData }) => {
    
    const [firstName, setFirstName] = useState(personData.firstName)
	const [lastName, setLastName] = useState(personData.lastName)
	const [summary, setSummary] = useState(personData.summary)
	const [homePhone, setHomePhone] = useState(personData.homePhone)
	const [mobilePhone, setMobilePhone] = useState(personData.mobilePhone)
	const [addressStreet, setAddressStreet] = useState(personData.addressStreet)
	const [addressTown, setAddressTown] = useState(personData.addressTown)
    const [addressPostcode, setAddressPostcode] = useState(personData.addressPostcode)

    return (
        <div className="bg-sky-900 bg-opacity-35 lg:w-1/2 md:w-2/3 w-full rounded p-4 mt-3 md:mx-0 m-3 flex flex-col gap-2 shadow-md">
            <form className="flex flex-col gap-2 max-w-md"onSubmit={(event) => handleEditPerson(event, personData.id, firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode, personData.imageId)} >
                <div>
                    <div className="mb-2 block">
                        <Label value="Name (required)" />
					</div>
					<div className="flex md:flex-row flex-col gap-1 w-full">
						<TextInput placeholder="First Name" className="flex-auto" required value={firstName} onChange={(e) => setFirstName(e.target.value)} />
						<TextInput placeholder="Last Name" className="flex-auto" required value={lastName} onChange={(e) => setLastName(e.target.value)}/>
					</div>
                </div>
                
                <div>
                    <div className="mb-2 block italic">
                        <Label value={`Summary (max 6000 characters, current: ${summary.length})`} />
                    </div>
                    <Textarea placeholder="A brilliant actor... " value={summary} onChange={(e) => setSummary(e.target.value)} rows={6} />
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
 

                <div className="grid grid-cols-2 justify-end px-2">
                    <button className="text-sm hover:underline font-bold text-center col-span-1" onClick={() => setEditMode(false)}>
                        Cancel Edit Mode
                    </button>
                    <button className="hover:underline bg-sky-900  p-2 py-1 rounded w-full text-white col-span-1 text-sm">
                        Confirm Edit
                    </button>
                </div>
            </form>
        </div>
    )
}

export default EditPersonForm