import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import EventService from "../../services/EventService.js";
import CustomSpinner from "../common/CustomSpinner.jsx";
import EventHighlight from "./EventHighlight.jsx";
import ContentCard from "../common/ContentCard.jsx";

const EventPage = () => {
  const eventId = useParams().id;

  const [event, setEvent] = useState(null);

  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const getEventData = async () => {
      const response = await EventService.getEventById(eventId);
      setEvent(response.data.event);
      setLoading(false);
    };
    getEventData();
  }, [eventId]);

  return (
    <ContentCard>
      {loading ? <CustomSpinner /> : <EventHighlight eventData={event} />}
    </ContentCard>
  );
};

export default EventPage;
