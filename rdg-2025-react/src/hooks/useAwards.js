import { useMutation, useQueryClient } from "@tanstack/react-query";
import AwardService from "../services/AwardService.js";

export const useAwards = () => {
  const queryClient = useQueryClient();

  const createAward = useMutation({
    mutationFn: ({ name, productionId, personId, festivalId }) =>
      AwardService.createNewAward(name, productionId, personId, festivalId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["awards"] });
    },
  });

  const updateAward = useMutation({
    mutationFn: ({ awardId, name, productionId, personId, festivalId }) =>
      AwardService.updateAward(awardId, name, productionId, personId, festivalId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["awards"] });
    },
  });

  const deleteAward = useMutation({
    mutationFn: ({ awardId }) => AwardService.deleteAward(awardId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["awards"] });
    },
  });

  return { createAward, updateAward, deleteAward };
};
