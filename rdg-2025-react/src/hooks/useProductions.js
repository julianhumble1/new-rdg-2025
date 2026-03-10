import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import ProductionService from "../services/ProductionService.js";

export const useProductions = () => {
  const queryClient = useQueryClient();

  const productions = useQuery({
    queryKey: ["productions"],
    queryFn: async () => {
      const response = await ProductionService.getAllProductions();
      return response.data.productions;
    },
    staleTime: 10 * 60 * 1000,
  });

  const createProduction = useMutation({
    mutationFn: ({
      name,
      venueId,
      author,
      description,
      auditionDate,
      sundowners,
      notConfirmed,
      flyerFile,
    }) =>
      ProductionService.createNewProduction(
        name,
        venueId,
        author,
        description,
        auditionDate,
        sundowners,
        notConfirmed,
        flyerFile,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["productions"] });
    },
  });

  const updateProduction = useMutation({
    mutationFn: ({
      productionId,
      name,
      venueId,
      author,
      description,
      auditionDate,
      sundowners,
      notConfirmed,
      flyerFile,
    }) =>
      ProductionService.updateProduction(
        productionId,
        name,
        venueId,
        author,
        description,
        auditionDate,
        sundowners,
        notConfirmed,
        flyerFile,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["productions"] });
    },
  });

  const updateProductionWithImage = useMutation({
    mutationFn: ({ productionData, imageId }) =>
      ProductionService.updateProductionWithImage(productionData, imageId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["productions"] });
    },
  });

  const deleteProduction = useMutation({
    mutationFn: ({ productionId }) =>
      ProductionService.deleteProduction(productionId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["productions"] });
    },
  });

  return {
    productions,
    createProduction,
    updateProduction,
    updateProductionWithImage,
    deleteProduction,
  };
};
