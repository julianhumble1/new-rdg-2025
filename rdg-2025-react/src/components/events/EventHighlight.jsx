import { format, parseISO } from "date-fns";
import HighlightListItem from "../common/HighlightListItem.jsx";
import HighlightTemplate from "../common/HighlightTemplate.jsx";
import { useState } from "react";
import EditEventForm from "./EditEventForm.jsx";
import ConfirmDeleteModal from "../modals/ConfirmDeleteModal.jsx";
import { useNavigate } from "react-router-dom";
import { useEvents } from "../../hooks/useEvents.js";

const EventHighlight = ({ eventData }) => {
  const [editMode, setEditMode] = useState(false);
  const [showConfirmDelete, setShowConfirmDelete] = useState(false);
  const [itemToDelete, setItemToDelete] = useState(null);
  const navigate = useNavigate();

  const handleDelete = (item) => {
    setShowConfirmDelete(true);
    setItemToDelete(item);
  };

  const { deleteEvent } = useEvents();

  const handleConfirmDelete = async (item) => {
    try {
      await deleteEvent.mutateAsync({ eventId: item.id });
      setShowConfirmDelete(false);
      navigate("/events");
    } catch (e) {
      console.error("Error deleting event:", e);
    }
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
              handleDelete={() => handleDelete(eventData)}
            >
              <HighlightListItem
                label="Description"
                value={eventData?.description}
              />
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
          {showConfirmDelete && (
            <ConfirmDeleteModal
              setShowConfirmDelete={setShowConfirmDelete}
              itemToDelete={itemToDelete}
              handleConfirmDelete={handleConfirmDelete}
            />
          )}
        </>
      )}
    </>
  );
};

export default EventHighlight;
