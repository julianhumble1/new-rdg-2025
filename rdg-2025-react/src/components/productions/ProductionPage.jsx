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
import PerformanceService from "../../services/PerformanceService.js";
import CreditsTabs from "../credits/CreditsTabs.jsx";
import CreditService from "../../services/CreditService.js";
import { Cloudinary } from "@cloudinary/url-gen/index";

const ProductionPage = () => {

    const productionId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const[image, setImage] = useState(null)

    const [productionData, setProductionData] = useState(null);
    const [performances, setPerformances] = useState([])

    const [actingCredits, setActingCredits] = useState([])
    const [musicianCredits, setMusicianCredits] = useState([])
    const [producerCredits, setProducerCredits] = useState([])

    const [editMode, setEditMode] = useState(searchParams.get("edit"))

    const [successMessage, setSuccessMessage] = useState("")
    const [errorMessage, setErrorMessage] = useState("")

    const [showConfirmDelete, setShowConfirmDelete] = useState(false);

    const [itemToDelete, setItemToDelete] = useState(null)

    const fetchProductionData = useCallback(async () => {
        const fetchProductionImage = async (imageId) => {
            const cld = new Cloudinary({ cloud: { cloudName: "dbher59sh" } })
            let img = cld.image("xrvbvweujcdqsjuuabys").format("auto").quality("auto")
            if (imageId !== "0") {
                try {
                    img = cld.image(imageId).format("auto").quality("auto")
                } catch (e) {
                    setErrorMessage(e.message)
                }
            }
            setImage(img)
        }



        try {
            const response = await ProductionService.getProductionById(productionId);
            setProductionData(response.data.production)
            setPerformances(response.data.performances)
            setActingCredits(response.data.actingCredits)
            setMusicianCredits(response.data.musicianCredits)
            setProducerCredits(response.data.producerCredits)
            await fetchProductionImage(response.data.production.flyerFile)
        } catch (e) {
            setErrorMessage(e.message)
        }
    }, [productionId])

    useEffect(() => {
        fetchProductionData()
    }, [fetchProductionData])


    const handleDelete = (item) => {
        setShowConfirmDelete(true)
        setItemToDelete(item)
    }

    const handleConfirmDelete = async (item) => {
        try {
            if (item.sundowners != null) {
                await ProductionService.deleteProduction(item.id)
                setShowConfirmDelete(false)
                navigate("/productions")
            } else if (item.time != null) {
                await PerformanceService.deletePerformance(item.id)
                setShowConfirmDelete(false)
                setErrorMessage("")
                setSuccessMessage("Successfully deleted performance")
                fetchProductionData()
            } else if (itemToDelete.type != null) {
            try {
                const response = await CreditService.deleteCreditById(item.id)
                fetchProductionData()
                setSuccessMessage(`Successfully deleted credit.`)
                setShowConfirmDelete(false)
            } catch (e) {
                setErrorMessage(e.message)
            }
        }  
        } catch (e) {
            setSuccessMessage("")
            setErrorMessage(e.message)
        }
    }

    const handleEdit = async (event, productionId, name, venueId, author, description, auditionDate, sundowners, notConfirmed, flyerFile) => {
        event.preventDefault()
        try {
            await ProductionService.updateProduction(
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
                <ConfirmDeleteModal setShowConfirmDelete={setShowConfirmDelete} itemToDelete={itemToDelete} handleConfirmDelete={ handleConfirmDelete } />
            }
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />

            <div className="flex justify-center w-full md:my-2 ">
                {(productionData && !editMode && performances.length > 0) &&
                    <div className="grid md:grid-cols-5 grid-cols-1 w-full md:w-4/5 md:shadow-md min-h-[26rem]">
                        <ProductionHighlight productionData={productionData} setEditMode={setEditMode} handleDelete={handleDelete} image={image} fetchProductionData={fetchProductionData} />
                        <PerformancesTable performances={performances} handleDelete={handleDelete} />
                    </div>
                }
                {(productionData && !editMode && performances.length === 0) &&
                    <div className="grid md:grid-cols-3 grid-cols-1 w-full lg:w-1/2 md:w-2/3 md:shadow-md h-[26rem]">
                        <ProductionHighlight productionData={productionData} setEditMode={setEditMode} handleDelete={handleDelete} />
                    </div>
                }
                {(productionData && editMode) &&
                    <EditProductionForm productionData={productionData} handleEdit={handleEdit} setEditMode={setEditMode} />
                }
            </div>

            <CreditsTabs actingCredits={actingCredits} musicianCredits={musicianCredits} producerCredits={producerCredits} creditsParent={"production"} handleDelete={handleDelete}/>
        
        </div>
    )
}

export default ProductionPage