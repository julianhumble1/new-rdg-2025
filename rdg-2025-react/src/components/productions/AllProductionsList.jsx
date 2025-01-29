import { useState, useEffect } from "react"
import ProductionService from "../../services/ProductionService.js"
import ProductionRow from "./ProductionRow.jsx"
import { useNavigate } from "react-router-dom"
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx"

const AllProductionsList = () => {

    const navigate = useNavigate();

    const [productions, setProductions] = useState([])

    const [showConfirmDelete, setShowConfirmDelete] = useState("");
    const [productionToDelete, setProductionToDelete] = useState(null)

    const [loading, setLoading] = useState(true)

    const [fetchError, setFetchError] = useState("")

    const [deleteError, setDeleteError] = useState("")
    const [deleteSuccess, setDeleteSuccess] = useState("")

    const handleDelete = (production) => {
        setShowConfirmDelete(true)
        setProductionToDelete(production)
    }

    const fetchAllProductions = async () => {
            try {
                const response = await ProductionService.getAllProductions()
                setProductions(response.data.productions)
                setLoading(false)
            } catch (e) {
                setFetchError(e.message)
                setLoading(false)
            }
        }

    useEffect(() => {
        fetchAllProductions()
    }, [])

    const handleConfirmDelete = async (production) => {
        try {
            await ProductionService.deleteProduction(production.id)
            setShowConfirmDelete(false)
            fetchAllProductions()
        } catch (e) {
            setDeleteError(e.message)
        }
    }

    return (<>
        <div>List of all productions</div>
        {fetchError && 
            <div className="text-red-500">
                {fetchError}
            </div>
        }
        {deleteError && 
            <div className="text-red-500">
                {deleteError}
            </div>
        }
        {deleteSuccess &&
            <div className="text-green-500">
                {deleteSuccess}
            </div>
        }
        {showConfirmDelete &&
            <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={productionToDelete} handleConfirmDelete={ handleConfirmDelete } />
        }
        {loading ? (
                <div>Loading...</div>
        ) : (
            productions && 
                <div className="mx-3">
                    <div className="grid grid-cols-12 bg-slate-400 italic font-bold">
                        <div className="col-span-2 p-1">Name</div>
                        <div className="col-span-2 p-1">Venue</div>
                        <div className="col-span-2 p-1">Description</div>
                        <div className="col-span-1 p-1">Sundowners</div>
                        <div className="col-span-2 p-1">Author</div>
                        <div className="col-span-1 p-1">Date Created</div>
                        <div className="col-span-2 p-1">Actions</div>
                    </div>
                    {productions.map((production, index) => (
                        <ProductionRow productionData={production} key={index} handleDelete={handleDelete}/>
                    ))}
                </div>
            )
                
        }


  </>)
}

export default AllProductionsList