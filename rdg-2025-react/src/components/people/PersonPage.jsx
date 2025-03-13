import { useCallback, useEffect, useState } from "react"
import { useNavigate, useParams, useSearchParams } from "react-router-dom"
import PersonService from "../../services/PersonService.js"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import PublicPersonHighlight from "./PublicPersonHighlight.jsx"
import DetailedPersonHighlight from "./DetailedPersonHighlight.jsx"
import EditPersonForm from "./EditPersonForm.jsx"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx"
import { Tabs } from "flowbite-react"
import { FilmIcon, MusicalNoteIcon, ScaleIcon } from "@heroicons/react/16/solid"
import CreditsTable from "../credits/CreditsTable.jsx"
import CreditsTabs from "../credits/CreditsTabs.jsx"

const PersonPage = () => {

    const personId = useParams().id
    const [searchParams] = useSearchParams();
    const navigate = useNavigate()

    const [personData, setPersonData] = useState(null)

    const [actingCredits, setActingCredits] = useState([])
    const [musicianCredits, setMusicianCredits] = useState([])
    const [producerCredits, setProducerCredits] = useState([])

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [viewType, setViewType] = useState("")

    const [itemToDelete, setItemToDelete] = useState("")
    const [showConfirmDelete, setShowConfirmDelete] = useState("")

    const handleDelete = (item) => {
        setItemToDelete(item)
        setShowConfirmDelete(true)
    }

    const fetchPersonData = useCallback(async () => {
        try {
            const response = await PersonService.getPersonById(personId)
            setViewType(response.data.responseType)
            setPersonData(response.data.person)
            setActingCredits(response.data.actingCredits)
            setMusicianCredits(response.data.musicianCredits)
            setProducerCredits(response.data.producerCredits)
            console.log(response)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [personId])

    useEffect(() => {
        fetchPersonData()
    }, [personId, fetchPersonData])

    const handleEditPerson = async (event, personId, firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode) => {
        event.preventDefault()
        try {
            const response = await PersonService.updatePerson(personId, firstName, lastName, summary, homePhone, mobilePhone, addressStreet, addressTown, addressPostcode)
            setSuccessMessage("Successfully edited!")
            setErrorMessage("")
            setEditMode(false)
            fetchPersonData()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    const handleConfirmDelete = async () => {
        try {
            const response = await PersonService.deletePersonById(personId)
            navigate("/people")
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            {showConfirmDelete &&
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={itemToDelete} handleConfirmDelete={ handleConfirmDelete } />
            }
            <div className="flex w-full justify-center">
                {viewType === "PUBLIC" && 
                    <PublicPersonHighlight personData={personData} />
                }
                {viewType === "DETAILED" && 
                    (editMode ?
                    <EditPersonForm setEditMode={setEditMode} handleEditPerson={handleEditPerson} personData={personData}/>
                    :
                    <DetailedPersonHighlight personData={personData} setEditMode={setEditMode} handleDelete={handleDelete} />)
                }
            </div>
            <CreditsTabs actingCredits={actingCredits} musicianCredits={musicianCredits} producerCredits={producerCredits} />
        </div>
    )
}

export default PersonPage