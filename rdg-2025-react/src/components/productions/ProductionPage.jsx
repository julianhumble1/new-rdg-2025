import { useState, useEffect, useCallback } from "react";
import { Link, useParams, useSearchParams } from "react-router-dom"
import ProductionService from "../../services/ProductionService.js";
import DateHelper from "../../utils/DateHelper.js";
import EditProductionForm from "./EditProductionForm.jsx";
import { format } from 'date-fns';

const ProductionPage = () => {

    const productionId = useParams().id;
    const [searchParams] = useSearchParams();

    const [productionData, setProductionData] = useState(null);

    const [fetchError, setFetchError] = useState("")

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [successMessage, setSuccessMessage] = useState("")
    const [failMessage, setFailMessage] = useState("")

    const fetchProductionData = useCallback(async () => {
        try {
            const response = await ProductionService.getProductionById(productionId);
            setProductionData(response.data.production)
        } catch (e) {
            setFailMessage(e.message)
        }
    }, [productionId])

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
            <div className="font-bold text-xl p-3 ">
                Production {productionId}: 
            </div>
            {fetchError &&
              <div>
                  {fetchError}
                </div>
            }
            {(productionData && !editMode) &&
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
                    </div>
                </div>

            }
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