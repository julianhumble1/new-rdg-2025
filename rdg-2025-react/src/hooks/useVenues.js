import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import VenueService from "../services/VenueService.js";

export const useVenues = () => {
  const queryClient = useQueryClient();

  const venues = useQuery({
    queryKey: ["venues"],
    queryFn: async () => {
      const response = await VenueService.getAllVenues();
      return response.data.venues;
    },
    staleTime: 10 * 60 * 1000,
  });

  const createVenue = useMutation({
    mutationFn: ({ name, address, town, postcode, notes, url }) =>
      VenueService.createNewVenue(name, address, town, postcode, notes, url),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["venues"] });
    },
  });

  const updateVenue = useMutation({
    mutationFn: ({ venueId, name, address, town, postcode, notes, url }) =>
      VenueService.updateVenue(venueId, name, address, town, postcode, notes, url),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["venues"] });
    },
  });

  const deleteVenue = useMutation({
    mutationFn: ({ venueId }) => VenueService.deleteVenue(venueId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["venues"] });
    },
  });

  return { venues, createVenue, updateVenue, deleteVenue };
};
