import { useMutation, useQuery, useQueryClient } from "@tanstack/react-query";
import PersonService from "../services/PersonService.js";

export const usePeople = () => {
  const queryClient = useQueryClient();

  const people = useQuery({
    queryKey: ["people"],
    queryFn: async () => {
      const response = await PersonService.getAllPeople();
      return response.data.people;
    },
    staleTime: 10 * 60 * 1000,
  });

  const createPerson = useMutation({
    mutationFn: ({
      firstName,
      lastName,
      summary,
      homePhone,
      mobilePhone,
      addressStreet,
      addressTown,
      addressPostcode,
    }) =>
      PersonService.addNewPerson(
        firstName,
        lastName,
        summary,
        homePhone,
        mobilePhone,
        addressStreet,
        addressTown,
        addressPostcode,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["people"] });
    },
  });

  const updatePerson = useMutation({
    mutationFn: ({
      personId,
      firstName,
      lastName,
      summary,
      homePhone,
      mobilePhone,
      addressStreet,
      addressTown,
      addressPostcode,
      imageId,
    }) =>
      PersonService.updatePerson(
        personId,
        firstName,
        lastName,
        summary,
        homePhone,
        mobilePhone,
        addressStreet,
        addressTown,
        addressPostcode,
        imageId,
      ),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["people"] });
    },
  });

  const updatePersonWithImage = useMutation({
    mutationFn: ({ personData, imageId }) =>
      PersonService.updatePersonWithImage(personData, imageId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["people"] });
    },
  });

  const deletePerson = useMutation({
    mutationFn: ({ personId }) => PersonService.deletePersonById(personId),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["people"] });
    },
  });

  return {
    people,
    createPerson,
    updatePerson,
    updatePersonWithImage,
    deletePerson,
  };
};
