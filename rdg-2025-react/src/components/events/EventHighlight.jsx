import { format, parseISO } from "date-fns";
import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import { useState } from "react";
import EditEventForm from "./EditEventForm.jsx";
import { useNavigate } from "react-router-dom";
import { useEvents } from "../../hooks/useEvents.js";

const EventHighlight = ({ eventData }) => {
    const [editMode, setEditMode] = useState(false);
    const navigate = useNavigate();
    const { deleteEvent } = useEvents();

    const handleDeleteEvent = async () => {
        await deleteEvent.mutateAsync({ eventId: eventData.id });
        navigate("/events");
    };

    return (
        <>
            {eventData && (
                <>
                    {editMode ? (
                        <EditEventForm eventData={eventData} setEditMode={setEditMode} />
                    ) : (
                        <HighlightTemplate
                            title={eventData.name}
                            type="Events"
                            handleEdit={() => setEditMode(true)}
                            handleDelete={handleDeleteEvent}
                        >
                            <HighlightListItem label="Description" value={eventData?.description} />
                            <HighlightListItem
                                label="Venue"
                                value={eventData.venue?.name}
                                link={`/venues/${eventData.venue?.id}`}
                            />
                            <HighlightListItem
                                label="Date & Time"
                                value={
                                    eventData?.dateTime
                                        ? format(parseISO(eventData.dateTime), "dd/MM/yyyy h:mm a")
                                        : "N/A"
                                }
                            />
                        </HighlightTemplate>
                    )}
                </>
            )}
        </>
    );
};

export default EventHighlight;
