import { useState, useCallback, useEffect } from "react";
import {
  useParams,
  useSearchParams,
  useNavigate,
  Link,
} from "react-router-dom";
import ProductionService from "../../services/ProductionService.js";
import VenueService from "../../services/VenueService.js";
import FestivalsTable from "../festivals/FestivalsTable.jsx";
import VenueHighlight from "./VenueHighlight.jsx";
import EditVenueForm from "./EditVenueForm.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import ProductionsTable from "../productions/ProductionsTable.jsx";
import { Tabs } from "flowbite-react";
import { FilmIcon, ScaleIcon } from "@heroicons/react/16/solid";
import FestivalService from "../../services/FestivalService.js";
import AltFestivalsTable from "../festivals/FestivalsTable.jsx";
import ContentCard from "../common/ContentCard.jsx";
import FeedbackToast from "../common/FeedbackToast.jsx";

const VenuePage = () => {
  const venueId = useParams().id;
  const [searchParams] = useSearchParams();
  const navigate = useNavigate();

  const [venueData, setVenueData] = useState(null);
  const [productions, setProductions] = useState([]);
  const [festivals, setFestivals] = useState([]);

  const [editMode, setEditMode] = useState(searchParams.get("edit"));

  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [itemToDelete, setItemToDelete] = useState(null);

  const [successMessage, setSuccessMessage] = useState("");
  const [errorMessage, setErrorMessage] = useState("");

  const fetchVenueData = useCallback(async () => {
    try {
      const response = await VenueService.getVenueById(venueId);
      setVenueData(response.data.venue);
      setProductions(response.data.productions);
      setFestivals(response.data.festivals);
    } catch (e) {
      setErrorMessage(e.message);
    }
  }, [venueId]);

  useEffect(() => {
    fetchVenueData();
  }, [fetchVenueData]);

  const handleDelete = (item) => {
    setItemToDelete(item);
    setShowConfirmDelete(true);
  };

  const handleConfirmDelete = async (item) => {
    try {
      // check whether item to delete is a production, festival or venue
      if (item.postcode == null && item.year == null) {
        // Production
        await ProductionService.deleteProduction(item.id);
        fetchVenueData();
      } else if (item.postcode == null) {
        // Festival
        await FestivalService.deleteFestivalById(item.id);
        fetchVenueData();
      } else {
        // Venue
        await VenueService.deleteVenue(item.id);
        navigate("/venues");
      }
      setShowConfirmDelete(false);
      setSuccessMessage(`Successfully deleted '${item.name}'`);
    } catch (e) {
      setErrorMessage(e.message);
    }
  };

  const handleEditVenue = async (
    event,
    id,
    name,
    address,
    town,
    postcode,
    notes,
    url,
  ) => {
    event.preventDefault();
    try {
      const response = await VenueService.updateVenue(
        id,
        name,
        address,
        town,
        postcode,
        notes,
        url,
      );
      setSuccessMessage("Successfully edited!");
      setErrorMessage("");
      fetchVenueData();
      setEditMode(false);
    } catch (e) {
      setSuccessMessage("");
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
        {venueData &&
          (editMode ? (
            <EditVenueForm
              venueData={venueData}
              handleEdit={handleEditVenue}
              setEditMode={setEditMode}
            />
          ) : (
            <VenueHighlight
              venueData={venueData}
              setEditMode={setEditMode}
              handleDelete={handleDelete}
            />
          ))}
      </ContentCard>
      <Tabs variant="underline" className="m-3">
        <Tabs.Item active title="Productions" icon={ScaleIcon}>
          {productions.length > 0 ? (
            <div className="m-2 overflow-auto">
              <ProductionsTable
                productions={productions}
                handleDelete={handleDelete}
                nameSearch={""}
                venueSearch={""}
                sundownersSearch={false}
              />
            </div>
          ) : (
            <div className="text-md font-bold ml-3">
              No productions at this venue
            </div>
          )}
        </Tabs.Item>
        <Tabs.Item title="Festivals" icon={FilmIcon}>
          {festivals.length > 0 ? (
            <div className="m-2 overflow-auto">
              <AltFestivalsTable
                festivals={festivals}
                handleDelete={handleDelete}
                nameSearch={""}
                venueSearch={""}
              />
            </div>
          ) : (
            <div className="text-md font-bold ml-3">
              No festivals at this venue
            </div>
          )}
        </Tabs.Item>
      </Tabs>
    </div>
  );
};

export default VenuePage;
