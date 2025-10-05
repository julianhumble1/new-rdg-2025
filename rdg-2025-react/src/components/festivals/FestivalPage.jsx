import { useState, useCallback, useEffect } from "react";
import { useParams, useNavigate, useSearchParams } from "react-router-dom";
import FestivalService from "../../services/FestivalService.js";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import FestivalHighlight from "./FestivalHighlight.jsx";
import PerformancesTable from "../performances/PerformancesTable.jsx";
import EditFestivalForm from "./EditFestivalForm.jsx";
import PerformanceService from "../../services/PerformanceService.js";
import AwardService from "../../services/AwardService.js";
import AwardsTabs from "../awards/AwardsTabs.jsx";
import ContentCard from "../common/ContentCard.jsx";

const FestivalPage = () => {
  const festivalId = useParams().id;
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const [festivalData, setFestivalData] = useState(null);

  const [performances, setPerformances] = useState([]);
  const [awards, setAwards] = useState([]);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [itemToDelete, setItemToDelete] = useState(false);

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

  const handleDelete = (item) => {
    setShowConfirmDelete(true);
    setItemToDelete(item);
  };

  const handleConfirmDelete = async (item) => {
    try {
      if (item.year != null) {
        await FestivalService.deleteFestivalById(item.id);
        navigate("/festivals");
      } else if (
        item.name &&
        (item.production || item.person || item.festival)
      ) {
        // This is an award
        await AwardService.deleteAward(item.id);
        setShowConfirmDelete(false);
        setErrorMessage("");
        setSuccessMessage("Successfully deleted award");
        getFestivalData();
      } else {
        await PerformanceService.deletePerformance(item.id);
        setShowConfirmDelete(false);
        setErrorMessage("");
        setSuccessMessage("Successfully deleted performance");
        getFestivalData();
      }
    } catch (e) {
      return;
    }
    setShowConfirmDelete(false);
  };

  const handleEdit = async (
    event,
    festivalId,
    name,
    venueId,
    year,
    month,
    description,
  ) => {
    event.preventDefault();
    try {
      const response = await FestivalService.updateFestival(
        festivalId,
        name,
        venueId,
        year,
        month,
        description,
      );
      setEditMode(false);
      setSuccessMessage("Successfully edited");
      getFestivalData();
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  return (
    <div>
      {showConfirmDelete && (
        <ConfirmDeleteModal
          setShowConfirmDelete={setShowConfirmDelete}
          itemToDelete={itemToDelete}
          handleConfirmDelete={handleConfirmDelete}
        />
      )}
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
                handleDelete={handleDelete}
              />
              <PerformancesTable
                performances={performances}
                handleDelete={handleDelete}
              />
            </div>
          ))}
      </ContentCard>

      {/* Awards Section */}
      {festivalData && !editMode && awards.length > 0 && (
        <AwardsTabs awards={awards} handleDelete={handleDelete} />
      )}
    </div>
  );
};

export default FestivalPage;
