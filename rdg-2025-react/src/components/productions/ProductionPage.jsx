import { useState, useCallback, useEffect } from "react";
import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import ProductionService from "../../services/ProductionService.js";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import EditProductionForm from "./EditProductionForm.jsx";
import { format } from "date-fns";
import ProductionHighlight from "./ProductionHighlight.jsx";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import CreditsTabs from "../credits/CreditsTabs.jsx";
import { Cloudinary } from "@cloudinary/url-gen/index";
import ContentCard from "../common/ContentCard.jsx";
import { useProductions } from "../../hooks/useProductions.js";
import { usePerformances } from "../../hooks/usePerformances.js";

const ProductionPage = () => {
    const productionId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const { deleteProduction, updateProduction } = useProductions();
    const { deletePerformance } = usePerformances();

    const [image, setImage] = useState(null);
    const [productionData, setProductionData] = useState(null);
    const [performances, setPerformances] = useState([]);
    const [actingCredits, setActingCredits] = useState([]);
    const [musicianCredits, setMusicianCredits] = useState([]);
    const [producerCredits, setProducerCredits] = useState([]);
    const [awards, setAwards] = useState([]);
    const [editMode, setEditMode] = useState(searchParams.get("edit"));
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");

    const fetchProductionData = useCallback(async () => {
        const fetchProductionImage = async (imageId) => {
            const cld = new Cloudinary({ cloud: { cloudName: "dbher59sh" } });
            let img = cld.image("xrvbvweujcdqsjuuabys").format("auto").quality("auto");
            if (imageId !== "0") {
                try {
                    img = cld.image(imageId).format("auto").quality("auto");
                } catch (e) {
                    setErrorMessage(e.message);
                }
            }
            setImage(img);
        };

        try {
            const response = await ProductionService.getProductionById(productionId);
            setProductionData(response.data.production);
            setPerformances(response.data.performances);
            setActingCredits(response.data.actingCredits);
            setMusicianCredits(response.data.musicianCredits);
            setProducerCredits(response.data.producerCredits);
            setAwards(response.data.awards || []);
            await fetchProductionImage(response.data.production.flyerFile);
        } catch (e) {
            setErrorMessage(e.message);
        }
    }, [productionId]);

    useEffect(() => {
        fetchProductionData();
    }, [fetchProductionData]);

    const handleDeleteProduction = async () => {
        await deleteProduction.mutateAsync({ productionId });
        navigate("/productions");
    };

    const handleDeletePerformance = async (performance) => {
        await deletePerformance.mutateAsync({ performanceId: performance.id });
        fetchProductionData();
    };

    const handleEdit = async (
        event,
        productionId,
        name,
        venueId,
        author,
        description,
        auditionDate,
        sundowners,
        notConfirmed,
        flyerFile,
    ) => {
        event.preventDefault();
        try {
            await updateProduction.mutateAsync({
                productionId,
                name,
                venueId,
                author,
                description,
                auditionDate: auditionDate ? format(auditionDate, "yyyy-MM-dd'T'HH:mm:ss.SSSSSSS") : "",
                sundowners,
                notConfirmed,
                flyerFile,
            });
            setSuccessMessage("Successfully Edited!");
            setErrorMessage("");
            fetchProductionData();
            setEditMode(false);
        } catch (e) {
            setSuccessMessage("");
            setErrorMessage(e.message);
        }
    };

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />

            <ContentCard>
                <div>
                    {productionData && editMode ? (
                        <EditProductionForm
                            productionData={productionData}
                            handleEdit={handleEdit}
                            setEditMode={setEditMode}
                        />
                    ) : (
                        <div className="flex gap-2 flex-col md:flex-row">
                            <ProductionHighlight
                                productionData={productionData}
                                setEditMode={setEditMode}
                                handleDelete={handleDeleteProduction}
                            />
                            <PerformancesTable
                                performances={performances}
                                handleDelete={handleDeletePerformance}
                            />
                        </div>
                    )}
                </div>
            </ContentCard>

            <CreditsTabs
                actingCredits={actingCredits}
                musicianCredits={musicianCredits}
                producerCredits={producerCredits}
                awards={awards}
                creditsParent={"production"}
                handleDelete={fetchProductionData}
            />
        </div>
    );
};

export default ProductionPage;
