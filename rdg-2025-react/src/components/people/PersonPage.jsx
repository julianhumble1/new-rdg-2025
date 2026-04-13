import { useCallback, useEffect, useState } from "react";
import { useNavigate, useParams, useSearchParams } from "react-router-dom";
import PersonService from "../../services/PersonService.js";
import SuccessMessage from "../modals/SuccessMessage.jsx";
import ErrorMessage from "../modals/ErrorMessage.jsx";
import PublicPersonHighlight from "./PublicPersonHighlight.jsx";
import EditPersonForm from "./EditPersonForm.jsx";
import CreditsTabs from "../credits/CreditsTabs.jsx";
import DetailedPersonHighlight from "./DetailedPersonHighlight.jsx";
import ContentCard from "../common/ContentCard.jsx";
import { usePeople } from "../../hooks/usePeople.js";

const PersonPage = () => {
    const personId = useParams().id;
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    const { updatePerson, deletePerson } = usePeople();

    const [personData, setPersonData] = useState(null);
    const [actingCredits, setActingCredits] = useState([]);
    const [musicianCredits, setMusicianCredits] = useState([]);
    const [producerCredits, setProducerCredits] = useState([]);
    const [awards, setAwards] = useState([]);

    const [editMode, setEditMode] = useState(searchParams.get("edit"));
    const [successMessage, setSuccessMessage] = useState("");
    const [errorMessage, setErrorMessage] = useState("");
    const [viewType, setViewType] = useState("");

    const fetchPersonData = useCallback(async () => {
        try {
            const response = await PersonService.getPersonById(personId);
            setViewType(response.data.responseType);
            setPersonData(response.data.person);
            setActingCredits(response.data.actingCredits);
            setMusicianCredits(response.data.musicianCredits);
            setProducerCredits(response.data.producerCredits);
            setAwards(response.data.awards || []);
        } catch (e) {
            setErrorMessage(e.message);
        }
    }, [personId]);

    useEffect(() => {
        fetchPersonData();
    }, [personId, fetchPersonData]);

    const handleDeletePerson = async () => {
        await deletePerson.mutateAsync({ personId });
        navigate("/people");
    };

    const handleEditPerson = async (
        event,
        personId,
        firstName,
        lastName,
        summary,
        homePhone,
        mobilePhone,
        addressStreet,
        addressTown,
        addressPostcode,
    ) => {
        event.preventDefault();
        try {
            await updatePerson.mutateAsync({
                personId,
                firstName,
                lastName,
                summary,
                homePhone,
                mobilePhone,
                addressStreet,
                addressTown,
                addressPostcode,
            });
            setSuccessMessage("Successfully edited!");
            setErrorMessage("");
            setEditMode(false);
            fetchPersonData();
        } catch (e) {
            setErrorMessage(e.message);
        }
    };

    return (
        <div>
            <SuccessMessage message={successMessage} />
            <ErrorMessage message={errorMessage} />
            <ContentCard>
                {viewType === "PUBLIC" && (
                    <PublicPersonHighlight personData={personData} />
                )}
                {viewType === "DETAILED" &&
                    (editMode ? (
                        <EditPersonForm
                            setEditMode={setEditMode}
                            handleEditPerson={handleEditPerson}
                            personData={personData}
                        />
                    ) : (
                        <DetailedPersonHighlight
                            personData={personData}
                            setEditMode={setEditMode}
                            handleDelete={handleDeletePerson}
                        />
                    ))}
            </ContentCard>
            <CreditsTabs
                actingCredits={actingCredits}
                musicianCredits={musicianCredits}
                producerCredits={producerCredits}
                awards={awards}
                creditsParent={"person"}
                handleDelete={fetchPersonData}
            />
        </div>
    );
};

export default PersonPage;
