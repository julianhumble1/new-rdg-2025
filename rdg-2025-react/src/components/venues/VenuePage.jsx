import { useState, useCallback, useEffect } from "react";
import { useParams, useSearchParams, useNavigate } from "react-router-dom";
import VenueService from "../../services/VenueService.js";
import VenueHighlight from "./VenueHighlight.jsx";
import EditVenueForm from "./EditVenueForm.jsx";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import ProductionsTable from "../productions/ProductionsTable.jsx";
import { Tabs } from "flowbite-react";
import { FilmIcon, ScaleIcon } from "@heroicons/react/16/solid";
import AltFestivalsTable from "../festivals/FestivalsTable.jsx";
import ContentCard from "../common/ContentCard.jsx";
import { useVenues } from "../../hooks/useVenues.js";
import { useFestivals } from "../../hooks/useFestivals.js";

const VenuePage = () => {
    const venueId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const { deleteVenue, updateVenue } = useVenues({ fetchList: false });
    const { deleteFestival } = useFestivals({ fetchList: false });

    const [venueData, setVenueData] = useState(null);
    const [productions, setProductions] = useState([]);
    const [festivals, setFestivals] = useState([]);

    const [editMode, setEditMode] = useState(searchParams.get("edit"));
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

    const handleDeleteVenue = async () => {
        await deleteVenue.mutateAsync({ venueId });
        navigate("/venues");
    };

    const handleDeleteFestival = async (festival) => {
        await deleteFestival.mutateAsync({ festivalId: festival.id });
        fetchVenueData();
    };

    const handleEditVenue = async (event, id, name, address, town, postcode, notes, url) => {
        event.preventDefault();
        try {
            await updateVenue.mutateAsync({ venueId: id, name, address, town, postcode, notes, url });
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
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />

            <ContentCard>
                {venueData &&
                    (editMode ? (
                        <EditVenueForm venueData={venueData} handleEdit={handleEditVenue} setEditMode={setEditMode} />
                    ) : (
                        <VenueHighlight venueData={venueData} setEditMode={setEditMode} handleDelete={handleDeleteVenue} />
                    ))}
            </ContentCard>
            <Tabs variant="underline" className="m-3">
                <Tabs.Item active title="Productions" icon={ScaleIcon}>
                    {productions.length > 0 ? (
                        <div className="m-2 overflow-auto">
                            <ProductionsTable productions={productions} />
                        </div>
                    ) : (
                        <div className="text-md font-bold ml-3">No productions at this venue</div>
                    )}
                </Tabs.Item>
                <Tabs.Item title="Festivals" icon={FilmIcon}>
                    {festivals.length > 0 ? (
                        <div className="m-2 overflow-auto">
                            <AltFestivalsTable
                                festivals={festivals}
                                handleDelete={handleDeleteFestival}
                                nameSearch={""}
                                venueSearch={""}
                            />
                        </div>
                    ) : (
                        <div className="text-md font-bold ml-3">No festivals at this venue</div>
                    )}
                </Tabs.Item>
            </Tabs>
        </div>
    );
};

export default VenuePage;
