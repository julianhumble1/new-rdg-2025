import { useState, useEffect, useCallback } from "react";
import { Link, useParams, useSearchParams, useNavigate } from "react-router-dom"
import ProductionService from "../../services/ProductionService.js";
import DateHelper from "../../utils/DateHelper.js";
import EditProductionForm from "./EditProductionForm.jsx";
import { format } from 'date-fns';
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import PerformanceRow from "../performances/PerformanceRow.jsx";

const ProductionPage = () => {

    const productionId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const [productionData, setProductionData] = useState(null);
    const [performances, setPerformances] = useState([])

    const [fetchError, setFetchError] = useState("")

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const [deleteError, setDeleteError] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false);

    const fetchProductionData = useCallback(async () => {
        try {
            const response = await ProductionService.getProductionById(productionId);
            setProductionData(response.data.production)
            setPerformances(response.data.performances)
            console.log(response)
        } catch (e) {
            setFailMessage(e.message)
        }
    }, [productionId])

    const handleDelete = () => {
        setShowConfirmDelete(true)
    }

    const handleConfirmDelete = async (production) => {
        try {
            await ProductionService.deleteProduction(production.id)
            setShowConfirmDelete(false)
            navigate("/productions")
        } catch (e) {
            setDeleteError(e.message)
        }
    }

    useEffect(() => {
        fetchProductionData()
    }, [fetchProductionData])

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
            console.log(response)
            setSuccessMessage("Successfully Edited!")
            setFailMessage("")
            fetchProductionData()
            setEditMode(false)
        } catch (e) {
            setSuccessMessage("")
            setFetchError(e.message)
        }
    }

    return (
        <div>
            {showConfirmDelete &&
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={productionData} handleConfirmDelete={ handleConfirmDelete } />
            }
            <div className="font-bold text-xl p-3 ">
                Production {productionId}: 
            </div>
            {fetchError &&
              <div>
                  {fetchError}
                </div>
            }
            {deleteError && 
                <div>
                    {deleteError}
                </div>
            }
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
                        <div>Audition Date: {productionData.auditionDate? DateHelper.formatDatabaseDateForDisplay(productionData.auditionDate) : "" }</div>
                        <div>Created: {DateHelper.formatDatabaseDateForDisplay(productionData.createdAt)} </div>
                        <div>Updated: {DateHelper.formatDatabaseDateForDisplay(productionData.updatedAt)}</div>
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
                {performances.length > 0 ? 
                    <div className="mx-3 w-2/3">
                        <div className="text-lg font-bold">
                            Performances:
                        </div>
                        <div className="grid grid-cols-4 bg-slate-400 italic font-bold">
                            <div className="col-span-1 p-1">Date & Time</div>
                            <div className="col-span-1 p-1">Venue</div>
                            <div className="col-span-1 p-1">Festival</div>
                            <div className="col-span-1 p-1">Description</div>
                        </div>
                        {performances.map((performance) => (<PerformanceRow key={performance.id } performanceData={performance}/>)) }
                    </div>
                    :
                    <div className="mx-3 font-bold">
                        No performances to display
                    </div>
                }
                
            </>}
            {(productionData && editMode) &&
                <div className="flex flex-row bg-gray-300 m-2 p-1 rounded w-2/3 justify-between">
                    <EditProductionForm productionData={productionData} handleEdit={handleEdit} />
                    
                    <div className="flex flex-row gap-3 items-start ">
                        <button className="text-blue-500 hover:text-blue-700 hover:underline" onClick={() => setEditMode(false)}>
                            Cancel Edit Mode
                        </button>
                    </div>

                    {failMessage && 
                    <div className="text-red-500">
                        Failed to edit venue: {failMessage}
                    </div>
                }
                </div>



            }
        </div>
    )
}

export default ProductionPage