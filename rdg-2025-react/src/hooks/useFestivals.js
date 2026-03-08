import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import FestivalService from "../services/FestivalService.js";

export const useFestivals = () => {
  const queryClient = useQueryClient();

  const festivals = useQuery({
    queryKey: ["festivals"],
    queryFn: async () => {
      const response = await FestivalService.getAllFestivals();
      return response.data.festivals;
    },
    staleTime: 10 * 60 * 1000,
  });

  const createFestival = useMutation({
    mutationFn: ({ name, venueId, year, month, description }) =>
      FestivalService.createNewFestival(name, venueId, year, month, description),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["festivals"] });
    },
  });

  const updateFestival = useMutation({
    mutationFn: ({ festivalId, name, venueId, year, month, description }) =>
      FestivalService.updateFestival(festivalId, name, venueId, year, month, description),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["festivals"] });
    },
  });

  const deleteFestival = useMutation({
    mutationFn: ({ festivalId }) => FestivalService.deleteFestivalById(festivalId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["festivals"] });
    },
  });

  return { festivals, createFestival, updateFestival, deleteFestival };
};
