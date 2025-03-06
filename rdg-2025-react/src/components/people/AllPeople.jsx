import { useEffect, useState } from "react"
import PersonService from "../../services/PersonService.js"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import { Label } from "flowbite-react"
import PeopleTable from "./PeopleTable.jsx"

const AllPeople = () => {

    const [people, setPeople] = useState([])
    const [responseType, setResponseType] = useState("")

	const [errorMessage, setErrorMessage] = useState("")

	const fetchAllPeople = async () => {
		try {
			const response = await PersonService.getAllPeople()
            console.log(response)
            setPeople(response.data.people)
            setResponseType(response.data.responseType)
		} catch (e) {
			setErrorMessage(e.message)
		}
	}

	useEffect(() => {
		fetchAllPeople() 
	}, [])

    return (
        <div>
            <ErrorMessage message={errorMessage} />
            <div className='grid lg:grid-cols-6 grid-cols-1'>
                <div className="col-span-1 rounded m-2 border bg-slate-200 max-h-fit drop-shadow-md lg:pb-6">
                    <div className='flex flex-col'>
                        <div className='font-bold lg:text-center m-2'>
							Filters
						</div>
						<div className="m-2">
							<Label value="Search" />
						</div>
                    </div>
                </div>

                <span className='col-span-5 p-2 pl-0 overflow-x-auto'>
                    <PeopleTable people={people} responseType={responseType } />
				</span>
            </div>
		</div>
  	)
}

export default AllPeople