import { useEffect, useState } from "react"
import { useParams } from "react-router-dom"
import PersonService from "../../services/PersonService.js"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import PublicPersonHighlight from "./PublicPersonHighlight.jsx"
import DetailedPersonHighlight from "./DetailedPersonHighlight.jsx"

const PersonPage = () => {

    const personId = useParams().id

    const [personData, setPersonData] = useState(null)

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [viewType, setViewType] = useState("")

    useEffect(() => {
        const fetchPersonData = async () => {
            try {
                const response = await PersonService.getPersonById(personId)
                setViewType(response.data.responseType)
                setPersonData(response.data.person)
                console.log(response)
            } catch (e) {
                setErrorMessage(e.message)
            }
        }

        fetchPersonData()
    }, [personId])

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            <div className="flex w-full justify-center">
                {viewType === "PUBLIC" && 
                    <PublicPersonHighlight personData={personData} />
                }
                {viewType === "DETAILED" && 
                    <DetailedPersonHighlight personData={personData} />
                }
            </div>
        </div>
    )
}

export default PersonPage