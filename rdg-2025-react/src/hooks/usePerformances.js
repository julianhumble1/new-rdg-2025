import { useMutation, useQueryClient } from "@tanstack/react-query";
import PerformanceService from "../services/PerformanceService.js";

export const usePerformances = () => {
  const queryClient = useQueryClient();

  const createPerformance = useMutation({
    mutationFn: ({
      productionId,
      venueId,
      festivalId,
      performanceTime,
      description,
      standardPrice,
      concessionPrice,
      boxOffice,
    }) =>
      PerformanceService.addNewPerformance(
        productionId,
        venueId,
        festivalId,
        performanceTime,
        description,
        standardPrice,
        concessionPrice,
        boxOffice,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["performances"] });
    },
  });

  const updatePerformance = useMutation({
    mutationFn: ({
      performanceId,
      productionId,
      venueId,
      festivalId,
      performanceTime,
      description,
      standardPrice,
      concessionPrice,
      boxOffice,
    }) =>
      PerformanceService.updatePerformance(
        performanceId,
        productionId,
        venueId,
        festivalId,
        performanceTime,
        description,
        standardPrice,
        concessionPrice,
        boxOffice,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["performances"] });
    },
  });

  const deletePerformance = useMutation({
    mutationFn: ({ performanceId }) =>
      PerformanceService.deletePerformance(performanceId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["performances"] });
    },
  });

  return { createPerformance, updatePerformance, deletePerformance };
};
