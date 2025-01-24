import { useState, useEffect, useCallback } from "react";
import { Link, useParams } from "react-router-dom"
import ProductionService from "../../services/ProductionService.js";
import DateHelper from "../../utils/DateHelper.js";

const ProductionPage = () => {

    const productionId = useParams().id;

    const [productionData, setProductionData] = useState(null);

    const [fetchError, setFetchError] = useState("")

    const fetchProductionData = useCallback(async () => {
        try {
            const response = await ProductionService.getProductionById(productionId);
            console.log(response)
            setProductionData(response.data.production)
        } catch (e) {
            setFetchError(e.message)
        }
    }, [productionId])

    useEffect(() => {
        fetchProductionData()
    }, [fetchProductionData])

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
            {productionData &&
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
                        <div>Created: {DateHelper.formatDatabaseDateForDisplay(productionData.createdAt)} </div>
                        <div>Updated: {DateHelper.formatDatabaseDateForDisplay(productionData.updatedAt)}</div>
                    </div>
                    
                </div>
            
            }
        </div>
    )
}

export default ProductionPage