import { useState, useEffect, useCallback } from "react";
import { Link, useParams, useSearchParams, useNavigate } from "react-router-dom"
import ProductionService from "../../services/ProductionService.js";
import EditProductionForm from "./EditProductionForm.jsx";
import { format } from 'date-fns';
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import PerformanceRow from "../performances/PerformanceRow.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import PerformancesTable from "../performances/PerformancesTable.jsx";

const ProductionPage = () => {

    const productionId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [productionData, setProductionData] = useState(null);
    const [performances, setPerformances] = useState([])

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false);

    const fetchProductionData = useCallback(async () => {
        try {
            const response = await ProductionService.getProductionById(productionId);
            setProductionData(response.data.production)
            setPerformances(response.data.performances)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [productionId])

    useEffect(() => {
        fetchProductionData()
    }, [fetchProductionData])


    const handleDelete = () => {
        setShowConfirmDelete(true)
    }

    const handleConfirmDelete = async (production) => {
        try {
            await ProductionService.deleteProduction(production.id)
            setShowConfirmDelete(false)
            navigate("/productions")
        } catch (e) {
            setErrorMessage(e.message)
        }
    }

    const handleEdit = async (event, productionId, name, venueId, author, description, auditionDate, sundowners, notConfirmed, flyerFile) => {
        event.preventDefault()
        try {
            const response = await ProductionService.updateProduction(
                productionId,
                name,
                venueId,
                author,
                description,
                auditionDate ? format(auditionDate, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS") : "",
                sundowners,
                notConfirmed,
                flyerFile
            )
            setSuccessMessage("Successfully Edited!")
            setErrorMessage("")
            fetchProductionData()
            setEditMode(false)
        } catch (e) {
            setSuccessMessage("")
            setErrorMessage(e.message)
        }
    }

    return (
        <div>

            {showConfirmDelete &&
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={productionData} handleConfirmDelete={ handleConfirmDelete } />
            }
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />


            <div className="font-bold text-xl p-3 ">
                Production {productionId}: 
            </div>

            {(productionData && !editMode) && <>
                <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                    <div>
                        <div className="font-bold"> {productionData.name}</div>
                        <div>Author: {productionData.author}</div>
                        <div>Sundowners: {productionData.sundowners ? "Yes" : "No"}</div>
                        <div>Description: {productionData.description}</div>
                        <div>Venue: {productionData.venue &&
                                <Link className="text-blue-500 hover:text-blue-700 hover:underline" to={`/venues/${productionData.venue.id}`}>{productionData.venue.name}</Link>
                            }
                        </div>
                        <div>Audition Date: {productionData.auditionDate? format(new Date(productionData.auditionDate), "MMMM d, yyyy, h:mm a") : "" }</div>
                        <div>Created: {format(new Date(productionData.createdAt), "dd-MM-yyyy")} </div>
                        <div>Updated: {format(new Date(productionData.updatedAt), "dd-MM-yyyy")}</div>
                    </div>
                    {successMessage && 
                    <div className="text-green-500">
                        {successMessage}
                    </div>
                }
                    <div className="flex flex-row gap-3 items-start ">
                        <button className="text-blue-500 hover:text-blue-700 hover:underline pr-2" onClick={() => setEditMode(true)}>
                            Edit
                        </button>
                        <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => handleDelete()}>
                            Delete
                        </button>
                    </div>
                </div>
                
                
            </>}
            {(productionData && editMode) &&
                <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                    <EditProductionForm productionData={productionData} handleEdit={handleEdit} />
                    
                    <div className="flex flex-row gap-3 items-start ">
                        <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(false)}>
                            Cancel Edit Mode
                        </button>
                    </div>

                    {errorMessage && 
                    <div className="text-red-500">
                        Failed to edit venue: {errorMessage}
                    </div>
                }
                </div>

            }

            <PerformancesTable performances={performances} />

        </div>
    )
}

export default ProductionPage