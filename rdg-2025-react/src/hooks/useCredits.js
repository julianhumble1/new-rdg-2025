import { useMutation, useQueryClient } from "@tanstack/react-query";
import CreditService from "../services/CreditService.js";

export const useCredits = () => {
  const queryClient = useQueryClient();

  const createCredit = useMutation({
    mutationFn: ({ name, type, productionId, personId, summary }) =>
      CreditService.addNewCredit(name, type, productionId, personId, summary),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["credits"] });
    },
  });

  const updateCredit = useMutation({
    mutationFn: ({ creditId, name, type, productionId, personId, summary }) =>
      CreditService.updateCredit(creditId, name, type, productionId, personId, summary),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["credits"] });
    },
  });

  const deleteCredit = useMutation({
    mutationFn: ({ creditId }) => CreditService.deleteCreditById(creditId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["credits"] });
    },
  });

  return { createCredit, updateCredit, deleteCredit };
};
