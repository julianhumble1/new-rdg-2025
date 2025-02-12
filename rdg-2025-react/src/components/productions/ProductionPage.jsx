import { useState, useCallback, useEffect } from "react";
import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import ProductionService from "../../services/ProductionService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import EditProductionForm from "./EditProductionForm.jsx";
import { format } from "date-fns";
import ProductionHighlight from "./ProductionHighlight.jsx";
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

            <div className="flex w-full justify-center">
                {(productionData && !editMode) &&
                    <ProductionHighlight productionData={productionData} setEditMode={setEditMode} handleDelete={handleDelete} />
                }
                {(productionData && editMode) &&
                    <EditProductionForm productionData={productionData} handleEdit={handleEdit} setEditMode={setEditMode} />
                }
            </div>

            <PerformancesTable performances={performances} />
        </div>
    )
}

export default ProductionPage