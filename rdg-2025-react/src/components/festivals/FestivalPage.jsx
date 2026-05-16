import { useState, useCallback, useEffect } from "react";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import FestivalService from "../../services/FestivalService.js";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import FestivalHighlight from "./FestivalHighlight.jsx";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import EditFestivalForm from "./EditFestivalForm.jsx";
import AwardsTabs from "../awards/AwardsTabs.jsx";
import ContentCard from "../common/ContentCard.jsx";
import { useFestivals } from "../../hooks/useFestivals.js";
import { usePerformances } from "../../hooks/usePerformances.js";

const FestivalPage = () => {
    const festivalId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const { deleteFestival, updateFestival } = useFestivals({ fetchList: false });
    const { deletePerformance } = usePerformances();

    const [festivalData, setFestivalData] = useState(null);
    const [performances, setPerformances] = useState([]);
    const [awards, setAwards] = useState([]);

    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [editMode, setEditMode] = useState(searchParams.get("edit"));

    const getFestivalData = useCallback(async () => {
        try {
            const response = await FestivalService.getFestivalById(festivalId);
            setFestivalData(response.data.festival);
            setPerformances(response.data.performances);
            setAwards(response.data.awards);
        } catch (e) {
            setErrorMessage(e.message);
        }
    }, [festivalId]);

    useEffect(() => {
        getFestivalData();
    }, [getFestivalData]);

    const handleDeleteFestival = async () => {
        await deleteFestival.mutateAsync({ festivalId });
        navigate("/festivals");
    };

    const handleDeletePerformance = async (performance) => {
        await deletePerformance.mutateAsync({ performanceId: performance.id });
        getFestivalData();
    };

    const handleEdit = async (event, festivalId, name, venueId, year, month, description) => {
        event.preventDefault();
        try {
            await updateFestival.mutateAsync({ festivalId, name, venueId, year, month, description });
            setEditMode(false);
            setSuccessMessage("Successfully edited");
            getFestivalData();
        } catch (e) {
            setErrorMessage(e.message);
        }
    };

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />

            <ContentCard>
                {festivalData &&
                    (editMode ? (
                        <EditFestivalForm
                            festivalData={festivalData}
                            handleEdit={handleEdit}
                            setEditMode={setEditMode}
                        />
                    ) : (
                        <div className="flex gap-2 flex-col md:flex-row">
                            <FestivalHighlight
                                festivalData={festivalData}
                                setEditMode={setEditMode}
                                handleDelete={handleDeleteFestival}
                            />
                            <PerformancesTable
                                performances={performances}
                                handleDelete={handleDeletePerformance}
                            />
                        </div>
                    ))}
            </ContentCard>

            {festivalData && !editMode && awards.length > 0 && (
                <AwardsTabs awards={awards} handleDelete={getFestivalData} />
            )}
        </div>
    );
};

export default FestivalPage;
