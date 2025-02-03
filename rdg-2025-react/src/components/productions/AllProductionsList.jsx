import { useState, useEffect } from "react"
import ProductionService from "../../services/ProductionService.js"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx"
import ErrorMessage from "../modals/ErrorMessage.jsx"
import SuccessMessage from "../modals/SuccessMessage.jsx"
import ProductionsTable from "./ProductionsTable.jsx"

const AllProductionsList = () => {

    const [productions, setProductions] = useState([])

    const [showConfirmDelete, setShowConfirmDelete] = useState("");
    const [productionToDelete, setProductionToDelete] = useState(null)

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const fetchAllProductions = async () => {
        try {
            const response = await ProductionService.getAllProductions()
            setProductions(response.data.productions)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    useEffect(() => {
        fetchAllProductions()
    }, [])

    const handleDelete = (production) => {
        setShowConfirmDelete(true)
        setProductionToDelete(production)
    }

    const handleConfirmDelete = async (production) => {
        try {
            await ProductionService.deleteProduction(production.id)
            setShowConfirmDelete(false)
            setSuccessMessage(`Successfully deleted ${production.name}`)
            fetchAllProductions()
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    return (<>
        <div>List of all productions</div>

        <SuccessMessage message={successMessage} />
        <ErrorMessage message={errorMessage} />

        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={productionToDelete} handleConfirmDelete={ handleConfirmDelete } />
        }

        <ProductionsTable productions={productions} handleDelete={handleDelete} />
        
  </>)
}

export default AllProductionsList