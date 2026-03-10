import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import EventService from "../services/EventService.js";

export const useEvents = () => {
  const queryClient = useQueryClient();

  const events = useQuery({
    queryKey: ["events"],
    queryFn: async () => {
      const response = await EventService.getAllEvents();
      return response.data.events;
    },
    staleTime: 10 * 60 * 1000,
  });

  const createEvent = useMutation({
    mutationFn: ({ name, description, dateTime, venue }) =>
      EventService.addNewEvent(name, description, dateTime, venue),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });

  const updateEvent = useMutation({
    mutationFn: ({ eventId, name, description, dateTime, venue }) =>
      EventService.updateEvent(eventId, name, description, dateTime, venue),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });

  const deleteEvent = useMutation({
    mutationFn: ({ eventId }) => EventService.deleteEvent(eventId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["events"] });
    },
  });

  return { events, createEvent, updateEvent, deleteEvent };
};
